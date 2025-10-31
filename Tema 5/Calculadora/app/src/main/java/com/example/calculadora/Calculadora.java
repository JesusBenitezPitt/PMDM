package com.example.calculadora;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Calculadora extends AppCompatActivity {

    protected TextView operacionActual;
    protected TextView resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        operacionActual = findViewById(R.id.operacionActual);
        resultado = findViewById(R.id.resultado);
        operacionActual.setText("");
        resultado.setText("");

        View.OnClickListener Listener = (View v) -> {
            Button b = (Button) v;
            if(!resultado.getText().toString().isEmpty()){
                resultado.setText("");
                operacionActual.setText(b.getText().toString());
            } else {
                operacionActual.append(b.getText().toString());
            }
        };

        findViewById(R.id.boton0).setOnClickListener(Listener);
        findViewById(R.id.boton1).setOnClickListener(Listener);
        findViewById(R.id.boton2).setOnClickListener(Listener);
        findViewById(R.id.boton3).setOnClickListener(Listener);
        findViewById(R.id.boton4).setOnClickListener(Listener);
        findViewById(R.id.boton5).setOnClickListener(Listener);
        findViewById(R.id.boton6).setOnClickListener(Listener);
        findViewById(R.id.boton7).setOnClickListener(Listener);
        findViewById(R.id.boton8).setOnClickListener(Listener);
        findViewById(R.id.boton9).setOnClickListener(Listener);

        findViewById(R.id.botonLimpiar).setOnClickListener((View v) -> {
            operacionActual.setText("");
            resultado.setText("");
        });

        findViewById(R.id.botonSumar).setOnClickListener(Listener("+"));
        findViewById(R.id.botonRestar).setOnClickListener(Listener("-"));
        findViewById(R.id.botonDividir).setOnClickListener(Listener("/"));
        findViewById(R.id.botonMultiplicar).setOnClickListener(Listener("x"));
        findViewById(R.id.botonPotencia).setOnClickListener(Listener("^"));


        findViewById(R.id.botonIgual).setOnClickListener((View v) -> {
            try {
                String operacion = operacionActual.getText().toString();
                String res = calcularOperacion(operacion);
                resultado.setText(String.valueOf(res));
            } catch (Exception e) {
                Toast.makeText(Calculadora.this, "No se puede hacer la operacion", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.botonDecimal).setOnClickListener((View v) -> {
            String textoActual = operacionActual.getText().toString();
            int ultimoOperadorIndex = Math.max(
                    Math.max(textoActual.lastIndexOf("+"), textoActual.lastIndexOf("-")),
                    Math.max(textoActual.lastIndexOf("*"), textoActual.lastIndexOf("/"))
            );

            String ultimoNumero = textoActual.substring(ultimoOperadorIndex + 1);

            if (!ultimoNumero.contains(".") && resultado.getText().toString().isEmpty()) {
                operacionActual.setText(operacionActual.getText().toString() + ".");
            }
        });
    }

    private View.OnClickListener Listener(String operador){
        return (View v) -> {
            String textoActual = operacionActual.getText().toString();
            if(Character.isDigit(textoActual.charAt(textoActual.length() - 1))){
                if(!resultado.getText().toString().isEmpty()){
                    operacionActual.setText(resultado.getText() + operador);
                    resultado.setText("");
                } else {
                    operacionActual.setText(operacionActual.getText() + operador);
                }
            }
        };
    };

    private static String calcularOperacion(String operacion) {
        operacion = operacion.replace("x", "*");
        double resul = 0.0;
        char operador = 0;
        String num = "";

        for (int i = 0; i < operacion.length(); i++) {
            char caracter = operacion.charAt(i);

            if (Character.isDigit(caracter) || caracter == '.') {
                num += caracter;
            } else {
                if (num.isEmpty() && caracter == '-' && (i == 0 || "+-*/".indexOf(operacion.charAt(i-1)) != -1)) {
                    num += caracter;
                    continue;
                }

                resul = realizarOperacion(resul, operador, num);
                operador = caracter;
                num = "";
            }
        }

        if (!num.isEmpty()) {
            resul = realizarOperacion(resul, operador, num);
        }

        if (resul == (int) resul) {
            return String.valueOf((int) resul);
        } else {
            String resStr = String.valueOf(resul);
            if (resStr.length() > 6) {
                resStr = String.format("%.5f", resul);
                while(resStr.endsWith("0")) resStr = resStr.substring(0, resStr.length()-1);
                if(resStr.endsWith(".")) resStr = resStr.substring(0, resStr.length()-1);
            }
            return resStr;
        }
    }

    private static double realizarOperacion(double acumulado, char op, String numero) {
        double n = Double.parseDouble(numero);
        if (op == 0) {
            return n;
        } else {
            switch (op) {
                case '+': return acumulado + n;
                case '-': return acumulado - n;
                case '*': return acumulado * n;
                case '/': return acumulado / n;
                case '^':
                    int num = (int) acumulado;
                    for(int i = 0; i < n-1; i++){
                        acumulado *= num;
                    }
                    return acumulado;
                default:  return acumulado;
            }
        }
    }
}