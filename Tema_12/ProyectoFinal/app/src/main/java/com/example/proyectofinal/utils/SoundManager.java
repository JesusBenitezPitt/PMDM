package com.example.proyectofinal.utils;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.ToneGenerator;

// Gestiona todo el audio del juego: efectos de sonido con ToneGenerator y música de fondo con AudioTrack.
public class SoundManager {

    private ToneGenerator toneGen;
    private AudioTrack    pistaMusicaFondo;
    private boolean       musicaActivada = true;
    private boolean       sfxActivado    = true;

    // Melodía de 8 notas (Hz) que se repite en bucle
    private static final int[] MELODIA     = {523, 659, 784, 659, 523, 440, 392, 440};
    private static final int TASA_MUESTREO = 22050;
    private static final int MUESTRAS_NOTA = TASA_MUESTREO / 4; // 250 ms por nota

    public SoundManager() {
        try {
            toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 45);
        } catch (Exception ignorado) {}
    }

    public void playCoin() {
        if (!sfxActivado || toneGen == null) return;
        toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 80);
    }

    public void playPowerUp() {
        if (!sfxActivado || toneGen == null) return;
        toneGen.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 300);
    }

    public void playTrampa() {
        if (!sfxActivado || toneGen == null) return;
        toneGen.startTone(ToneGenerator.TONE_CDMA_LOW_L, 180);
    }

    public void playHit() {
        if (!sfxActivado || toneGen == null) return;
        toneGen.startTone(ToneGenerator.TONE_CDMA_LOW_L, 250);
    }

    public void playGameOver() {
        if (!sfxActivado || toneGen == null) return;
        toneGen.startTone(ToneGenerator.TONE_CDMA_CALLDROP_LITE, 600);
    }

    public void playLevelComplete() {
        if (!sfxActivado || toneGen == null) return;
        toneGen.startTone(ToneGenerator.TONE_CDMA_HIGH_PBX_SLS, 500);
    }

    public void startMusic() {
        if (!musicaActivada || pistaMusicaFondo != null) return;
        try {
            short[] pcm    = construirMelodia();
            int     bytes  = pcm.length * 2;

            pistaMusicaFondo = new AudioTrack(
                new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build(),
                new AudioFormat.Builder()
                    .setSampleRate(TASA_MUESTREO)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build(),
                bytes,
                AudioTrack.MODE_STATIC,
                AudioManager.AUDIO_SESSION_ID_GENERATE
            );
            pistaMusicaFondo.write(pcm, 0, pcm.length);
            pistaMusicaFondo.setLoopPoints(0, pcm.length, -1); // bucle infinito
            pistaMusicaFondo.play();
        } catch (Exception e) {
            pistaMusicaFondo = null;
        }
    }

    public void stopMusic() {
        if (pistaMusicaFondo != null) {
            try {
                pistaMusicaFondo.stop();
                pistaMusicaFondo.release();
            } catch (Exception ignorado) {}
            pistaMusicaFondo = null;
        }
    }

    public void pauseMusic() {
        if (pistaMusicaFondo != null) {
            try { pistaMusicaFondo.pause(); } catch (Exception ignorado) {}
        }
    }

    public void resumeMusic() {
        if (pistaMusicaFondo != null && musicaActivada) {
            try { pistaMusicaFondo.play(); } catch (Exception ignorado) {}
        }
    }

    // Genera el buffer PCM de la melodía con envolvente ADSR sencilla.
    private short[] construirMelodia() {
        int    totalMuestras = MELODIA.length * MUESTRAS_NOTA;
        short[] buf          = new short[totalMuestras];

        for (int n = 0; n < MELODIA.length; n++) {
            int freq = MELODIA[n];
            int base = n * MUESTRAS_NOTA;

            for (int i = 0; i < MUESTRAS_NOTA; i++) {
                double t      = (double) i / TASA_MUESTREO;
                double muestra = Math.sin(2 * Math.PI * freq * t);

                // Envolvente: ataque 10 ms, liberación 30 ms
                double env = 1.0;
                int atk = (int)(TASA_MUESTREO * 0.01);
                int rel = (int)(TASA_MUESTREO * 0.03);
                if (i < atk) env = (double) i / atk;
                else if (i > MUESTRAS_NOTA - rel) env = (double)(MUESTRAS_NOTA - i) / rel;

                buf[base + i] = (short)(muestra * env * 7000);
            }
        }
        return buf;
    }

    public void release() {
        stopMusic();
        if (toneGen != null) {
            toneGen.release();
            toneGen = null;
        }
    }

    public void setMusicaActivada(boolean activada) {
        musicaActivada = activada;
        if (!activada) pauseMusic();
        else resumeMusic();
    }

    public void setSfxActivado(boolean activado) {
        sfxActivado = activado;
    }
}
