#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define NUM_INPUT 10       // Número de entradas
#define NUM_HIDDEN 20      // Número de neurônios na camada oculta
#define NUM_OUTPUT 1       // Número de saídas
#define MAX_EPOCHS 1000    // Máximo de épocas de treinamento
#define ERROR_THRESHOLD 0.01 // Limite de erro para treinamento

// Estrutura para a rede neural
typedef struct {
    double weights_input_hidden[NUM_INPUT][NUM_HIDDEN];
    double weights_hidden_output[NUM_HIDDEN][NUM_OUTPUT];
    double hidden_layer[NUM_HIDDEN];
    double output_layer[NUM_OUTPUT];
    double learning_rate;
} NeuralNetwork;

// Função de ativação Sigmoid
double sigmoid(double x) {
    return 1 / (1 + exp(-x));
}

// Derivada da função Sigmoid
double sigmoid_derivative(double x) {
    return x * (1 - x);
}

// Inicializa a rede neural com pesos aleatórios
void initialize_network(NeuralNetwork *nn) {
    nn->learning_rate = 0.01;
    for (int i = 0; i < NUM_INPUT; i++) {
        for (int j = 0; j < NUM_HIDDEN; j++) {
            nn->weights_input_hidden[i][j] = ((double)rand() / RAND_MAX) * 2 - 1; // Peso aleatório entre -1 e 1
        }
    }
    for (int j = 0; j < NUM_HIDDEN; j++) {
        for (int k = 0; k < NUM_OUTPUT; k++) {
            nn->weights_hidden_output[j][k] = ((double)rand() / RAND_MAX) * 2 - 1; // Peso aleatório entre -1 e 1
        }
    }
}

// Função de treinamento da rede neural
void train(NeuralNetwork *nn, double input[][NUM_INPUT], double output[][NUM_OUTPUT], int num_samples) {
    for (int epoch = 0; epoch < MAX_EPOCHS; epoch++) {
        double total_error = 0;
        for (int s = 0; s < num_samples; s++) {
            // Feedforward
            for (int j = 0; j < NUM_HIDDEN; j++) {
                double activation = 0;
                for (int i = 0; i < NUM_INPUT; i++) {
                    activation += input[s][i] * nn->weights_input_hidden[i][j];
                }
                nn->hidden_layer[j] = sigmoid(activation);
            }

            for (int k = 0; k < NUM_OUTPUT; k++) {
                double activation = 0;
                for (int j = 0; j < NUM_HIDDEN; j++) {
                    activation += nn->hidden_layer[j] * nn->weights_hidden_output[j][k];
                }
                nn->output_layer[k] = sigmoid(activation);
            }

            // Cálculo do erro
            double error = output[s][0] - nn->output_layer[0];
            total_error += error * error;

            // Backpropagation
            double output_delta = error * sigmoid_derivative(nn->output_layer[0]);
            for (int j = 0; j < NUM_HIDDEN; j++) {
                nn->weights_hidden_output[j][0] += nn->learning_rate * output_delta * nn->hidden_layer[j];
            }

            for (int j = 0; j < NUM_HIDDEN; j++) {
                double hidden_delta = output_delta * nn->weights_hidden_output[j][0] * sigmoid_derivative(nn->hidden_layer[j]);
                for (int i = 0; i < NUM_INPUT; i++) {
                    nn->weights_input_hidden[i][j] += nn->learning_rate * hidden_delta * input[s][i];
                }
            }
        }

        total_error /= num_samples;
        if (total_error < ERROR_THRESHOLD) {
            printf("Training stopped early at epoch %d\n", epoch);
            break;
        }
    }
}

// Função para prever com a rede neural treinada
void predict(NeuralNetwork *nn, double input[NUM_INPUT]) {
    for (int j = 0; j < NUM_HIDDEN; j++) {
        double activation = 0;
        for (int i = 0; i < NUM_INPUT; i++) {
            activation += input[i] * nn->weights_input_hidden[i][j];
        }
        nn->hidden_layer[j] = sigmoid(activation);
    }

    for (int k = 0; k < NUM_OUTPUT; k++) {
        double activation = 0;
        for (int j = 0; j < NUM_HIDDEN; j++) {
            activation += nn->hidden_layer[j] * nn->weights_hidden_output[j][k];
        }
        nn->output_layer[k] = sigmoid(activation);
    }

    printf("Prediction: %f\n", nn->output_layer[0]);
}

int main() {
    NeuralNetwork nn;
    initialize_network(&nn);

    // Dados de treinamento fictícios
    double input[2][NUM_INPUT] = {
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    double output[2][NUM_OUTPUT] = {
        {1},
        {1}
    };

    // Treinar a rede neural
    train(&nn, input, output, 2);

    // Realizar previsões
    double new_input[NUM_INPUT] = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    predict(&nn, new_input);

    return 0;
}
