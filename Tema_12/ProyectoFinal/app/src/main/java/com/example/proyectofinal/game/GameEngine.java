package com.example.proyectofinal.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Motor principal del juego: gestiona el estado, las entidades y la lógica de cada frame.
// GameView llama a update() por frame y lee el estado para renderizar.
public class GameEngine {

    // Estados posibles de la partida
    public enum State { PLAYING, PAUSED, DYING, LEVEL_COMPLETE, GAME_OVER }

    private volatile State estado = State.PLAYING;

    private int puntuacion  = 0;
    private int vidas       = GameConstants.VIDAS_INICIALES;
    private int numeroNivel = 1;

    // Datos del nivel
    private int[][] laberinto;
    private int filaInicio     = 1, columnaInicio     = 1;
    private int filaSalida, columnaSalida;

    // Entidades del juego
    private Player         jugador;
    private List<Enemy>    enemigos;
    private List<Collectible> monedas;
    private List<PowerUp>  powerUps;
    private List<Trampa>   trampas;

    // Temporizadores de power-ups (ms restantes)
    private long timerVelocidad = 0;
    private long timerEscudo    = 0;
    private long timerLento     = 0;

    // Temporizadores de transición de estado
    private long timerMuerte        = 0;
    private long timerNivelCompleto = 0;

    // Periodo de gracia tras respawn: el perseguidor rojo no ataca al jugador
    private long timerGracia = 0;

    // Tiempo de juego del nivel actual y bonus obtenido al completarlo
    private long tiempoNivelMs = 0;
    private int  bonusTiempo   = 0;

    // Callbacks hacia la Activity para sonido y navegación
    public interface Callback {
        void onCoinCollected();
        void onPowerUpCollected(int tipo);
        void onTrampaActivada();
        void onPlayerHit();
        void onLevelComplete(int nivel, int puntuacion);
        void onGameOver(int puntuacion);
    }

    private final Callback callback;

    public GameEngine(Callback callback) {
        this.callback = callback;
        iniciarNivel(1);
    }

    // Actualiza toda la lógica del juego según los ms transcurridos desde el último frame.
    public void update(long deltaMs) {
        float delta = deltaMs / 1000f;

        switch (estado) {
            case DYING:
                timerMuerte += deltaMs;
                jugador.update(delta, laberinto); // mantiene la animación de parpadeo
                if (timerMuerte >= GameConstants.DURACION_MUERTE_MS) {
                    timerMuerte = 0;
                    if (vidas <= 0) {
                        estado = State.GAME_OVER;
                        if (callback != null) callback.onGameOver(puntuacion);
                    } else {
                        respawnJugador();
                        estado = State.PLAYING;
                    }
                }
                break;

            case LEVEL_COMPLETE:
                timerNivelCompleto += deltaMs;
                if (timerNivelCompleto >= GameConstants.DURACION_NIVEL_COMPLETO_MS) {
                    iniciarNivel(numeroNivel + 1);
                }
                break;

            case PLAYING:
                tiempoNivelMs += deltaMs;
                actualizarTimersPowerUps(deltaMs);
                actualizarGracia(deltaMs);
                jugador.update(delta, laberinto);
                comprobarRecogidas();
                comprobarTrampas();
                comprobarSalida();
                actualizarEnemigos(delta);
                comprobarColisionesEnemigos();
                break;

            default:
                break;
        }
    }

    public void setPlayerDirection(int direccion) {
        if (estado == State.PLAYING) {
            jugador.setPendingDirection(direccion);
        }
    }

    public void togglePause() {
        if (estado == State.PLAYING)  estado = State.PAUSED;
        else if (estado == State.PAUSED) estado = State.PLAYING;
    }

    public void restart() {
        puntuacion  = 0;
        vidas       = GameConstants.VIDAS_INICIALES;
        iniciarNivel(1);
    }

    // Genera un nuevo nivel: laberinto procedural, monedas, power-ups, trampas y enemigos.
    private void iniciarNivel(int nivel) {
        this.numeroNivel       = nivel;
        this.timerNivelCompleto = 0;
        this.timerVelocidad    = 0;
        this.timerEscudo       = 0;
        this.timerLento        = 0;
        this.tiempoNivelMs     = 0;
        this.bonusTiempo       = 0;

        MazeGenerator generador = new MazeGenerator(
                GameConstants.FILAS_LABERINTO, GameConstants.COLUMNAS_LABERINTO);
        laberinto = generador.generate(System.currentTimeMillis() + nivel * 997L);

        filaSalida    = GameConstants.FILAS_LABERINTO - 2;
        columnaSalida = GameConstants.COLUMNAS_LABERINTO - 2;
        laberinto[filaSalida][columnaSalida] = GameConstants.CAMINO;

        // Monedas en todas las celdas transitables excepto inicio y salida
        monedas = new ArrayList<>();
        for (int f = 0; f < laberinto.length; f++) {
            for (int c = 0; c < laberinto[0].length; c++) {
                if (laberinto[f][c] == GameConstants.CAMINO
                        && !(f == filaInicio && c == columnaInicio)
                        && !(f == filaSalida && c == columnaSalida)) {
                    monedas.add(new Collectible(f, c));
                }
            }
        }

        colocarPowerUps(nivel);
        colocarTrampas(nivel);

        jugador = new Player(filaInicio, columnaInicio);
        generarEnemigos(nivel);

        estado = State.PLAYING;
    }

    // Sustituye algunas monedas por power-ups distribuidos por tipo de forma cíclica.
    private void colocarPowerUps(int nivel) {
        powerUps = new ArrayList<>();
        if (monedas.isEmpty()) return;

        int cantidad = Math.min(6, monedas.size() / 4 + 3);
        Random rand  = new Random(nivel * 31L);
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            int idx;
            int intentos = 0;
            do {
                idx = rand.nextInt(monedas.size());
                intentos++;
            } while (indices.contains(idx) && intentos < 50);

            if (!indices.contains(idx)) {
                indices.add(idx);
                Collectible m = monedas.get(idx);
                powerUps.add(new PowerUp(m.getRow(), m.getCol(), i % 3));
            }
        }

        indices.sort((a, b) -> b - a);
        for (int idx : indices) monedas.remove(idx);
    }

    // Sustituye algunas monedas por trampas; el número aumenta con el nivel.
    private void colocarTrampas(int nivel) {
        trampas = new ArrayList<>();
        if (monedas.isEmpty()) return;

        int cantidad = Math.min(2 + nivel, monedas.size() / 3);
        Random rand  = new Random(nivel * 79L);
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            int idx;
            int intentos = 0;
            do {
                idx = rand.nextInt(monedas.size());
                intentos++;
            } while (indices.contains(idx) && intentos < 50);

            if (!indices.contains(idx)) {
                indices.add(idx);
                Collectible m = monedas.get(idx);
                trampas.add(new Trampa(m.getRow(), m.getCol()));
            }
        }

        indices.sort((a, b) -> b - a);
        for (int idx : indices) monedas.remove(idx);
    }

    // Crea los enemigos del nivel con tipos y velocidades escaladas.
    private void generarEnemigos(int nivel) {
        enemigos = new ArrayList<>();

        int   cantidad  = Math.min(2 + nivel, 7);
        float velocBase = 1.8f + nivel * 0.35f;

        // Solo candidatos suficientemente alejados del punto de inicio
        List<int[]> candidatos = new ArrayList<>();
        for (int f = 0; f < laberinto.length; f++) {
            for (int c = 0; c < laberinto[0].length; c++) {
                if (laberinto[f][c] == GameConstants.CAMINO) {
                    int dist = MazeGenerator.manhattan(f, c, filaInicio, columnaInicio);
                    if (dist >= 8) candidatos.add(new int[]{f, c});
                }
            }
        }

        Random rand = new Random(nivel * 53L);
        for (int i = 0; i < cantidad && !candidatos.isEmpty(); i++) {
            int idx   = rand.nextInt(candidatos.size());
            int[] pos = candidatos.remove(idx);

            Enemy enemigo;
            switch (i % 3) {
                case 0:
                    // Perseguidor: usa BFS para encontrar al jugador
                    enemigo = new ChaserEnemy(pos[0], pos[1], velocBase);
                    break;
                case 1:
                    // Aleatorio: elige direcciones al azar en cada intersección
                    enemigo = new RandomEnemy(pos[0], pos[1], velocBase * 0.75f);
                    break;
                default:
                    // Patrullero: avanza en línea recta y rebota en paredes
                    enemigo = new PatrolEnemy(pos[0], pos[1], velocBase * 0.85f);
                    break;
            }
            enemigos.add(enemigo);
        }
    }

    // Descuenta el timer de gracia y quita el modo huida cuando expira.
    private void actualizarGracia(long deltaMs) {
        if (timerGracia <= 0) return;
        timerGracia -= deltaMs;
        if (timerGracia <= 0) {
            timerGracia = 0;
            for (Enemy e : enemigos) e.setModoHuida(false);
        }
    }

    private void actualizarTimersPowerUps(long deltaMs) {
        if (timerVelocidad > 0) {
            timerVelocidad -= deltaMs;
            if (timerVelocidad <= 0) { timerVelocidad = 0; jugador.setSpeedBoost(false); }
        }
        if (timerEscudo > 0) {
            timerEscudo -= deltaMs;
            if (timerEscudo <= 0) { timerEscudo = 0; jugador.setShielded(false); }
        }
        if (timerLento > 0) {
            timerLento -= deltaMs;
            if (timerLento <= 0) {
                timerLento = 0;
                for (Enemy e : enemigos) e.setSlowed(false);
            }
        }
    }

    private void actualizarEnemigos(float delta) {
        for (Enemy e : enemigos) {
            e.update(delta, laberinto, jugador);
        }
    }

    private void comprobarRecogidas() {
        int fj = jugador.getGridRow();
        int cj = jugador.getGridCol();

        for (int i = monedas.size() - 1; i >= 0; i--) {
            Collectible m = monedas.get(i);
            if (m.getRow() == fj && m.getCol() == cj) {
                monedas.remove(i);
                puntuacion += GameConstants.PUNTOS_MONEDA;
                if (callback != null) callback.onCoinCollected();
            }
        }

        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp p = powerUps.get(i);
            if (p.getRow() == fj && p.getCol() == cj) {
                powerUps.remove(i);
                puntuacion += GameConstants.PUNTOS_POWERUP;
                aplicarPowerUp(p.getType());
                if (callback != null) callback.onPowerUpCollected(p.getType());
            }
        }
    }

    private void comprobarTrampas() {
        int fj = jugador.getGridRow();
        int cj = jugador.getGridCol();

        for (Trampa t : trampas) {
            if (!t.isActivada() && t.getFila() == fj && t.getColumna() == cj) {
                t.activar();
                // Penalización: la puntuación nunca baja de 0
                puntuacion = Math.max(0, puntuacion + GameConstants.PENALIZACION_TRAMPA);
                if (callback != null) callback.onTrampaActivada();
            }
        }
    }

    private void aplicarPowerUp(int tipo) {
        switch (tipo) {
            case PowerUp.TYPE_SPEED:
                timerVelocidad = GameConstants.DURACION_POWERUP_MS;
                jugador.setSpeedBoost(true);
                break;
            case PowerUp.TYPE_SHIELD:
                timerEscudo = GameConstants.DURACION_POWERUP_MS;
                jugador.setShielded(true);
                break;
            case PowerUp.TYPE_SLOW:
                timerLento = GameConstants.DURACION_POWERUP_MS;
                for (Enemy e : enemigos) e.setSlowed(true);
                break;
        }
    }

    private void comprobarSalida() {
        if (jugador.getGridRow() == filaSalida && jugador.getGridCol() == columnaSalida
                && !jugador.isMoving()) {

            // Bonus por tiempo: más rápido = más puntos (máximo con SEGUNDOS_BONUS_MAX s)
            int segundosUsados = (int)(tiempoNivelMs / 1000);
            bonusTiempo = Math.max(0, GameConstants.SEGUNDOS_BONUS_MAX - segundosUsados) * 3;

            puntuacion += numeroNivel * GameConstants.PUNTOS_NIVEL + bonusTiempo;
            estado = State.LEVEL_COMPLETE;
            timerNivelCompleto = 0;
            if (callback != null) callback.onLevelComplete(numeroNivel, puntuacion);
        }
    }

    private void comprobarColisionesEnemigos() {
        // Sin daño si el jugador tiene escudo, está muriendo o está en periodo de gracia
        if (jugador.isShielded() || estado == State.DYING || timerGracia > 0) return;

        float fj = jugador.getPixelRow();
        float cj = jugador.getPixelCol();

        for (Enemy e : enemigos) {
            float df   = fj - e.getPixelRow();
            float dc   = cj - e.getPixelCol();
            float dist = (float) Math.sqrt(df * df + dc * dc);

            if (dist < 0.65f) {
                golpearJugador();
                return;
            }
        }
    }

    private void golpearJugador() {
        vidas--;
        estado = State.DYING;
        timerMuerte = 0;
        jugador.setDying(true);
        if (callback != null) callback.onPlayerHit();
    }

    private void respawnJugador() {
        jugador.respawn(filaInicio, columnaInicio);
        // Activar periodo de gracia: el perseguidor rojo se dispersa durante 3 segundos
        timerGracia = GameConstants.DURACION_GRACIA_MS;
        for (Enemy e : enemigos) e.setModoHuida(true);
    }

    // Getters de solo lectura para el renderizador

    public State   getState()            { return estado; }
    public int     getScore()            { return puntuacion; }
    public int     getLives()            { return vidas; }
    public int     getLevelNumber()      { return numeroNivel; }
    public int[][] getMaze()             { return laberinto; }
    public int     getExitRow()          { return filaSalida; }
    public int     getExitCol()          { return columnaSalida; }
    public int     getBonusTiempo()      { return bonusTiempo; }
    public long    getTiempoNivelMs()    { return tiempoNivelMs; }

    public Player            getPlayer()       { return jugador; }
    public List<Enemy>       getEnemies()      { return enemigos; }
    public List<Collectible> getCollectibles() { return monedas; }
    public List<PowerUp>     getPowerUps()     { return powerUps; }
    public List<Trampa>      getTrampas()      { return trampas; }

    public long getSpeedTimer()         { return timerVelocidad; }
    public long getShieldTimer()        { return timerEscudo; }
    public long getSlowTimer()          { return timerLento; }
    public long getLevelCompleteTimer() { return timerNivelCompleto; }
    public long getGraceTimer()         { return timerGracia; }
}
