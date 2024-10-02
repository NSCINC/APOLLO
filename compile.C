#include <stdio.h>

// Tipo de função que simula o comportamento do Supplier<Void>
typedef void (*VoidFunction)();

// Função que desativa a lógica de otimizações de uma API
void disable(VoidFunction fn, int recursive) {
    printf("Disabling with recursive = %d\n", recursive);
    // Execute a função fornecida, se necessário
    fn();
}

// Função que simula a desativação de otimizações do Dynamo
VoidFunction disableDynamo(VoidFunction fn, int recursive) {
    return () -> {
        disable(fn, recursive);
    };
}

// Função que envolve a lógica de desativação em um decorador
VoidFunction disableDynamoWrapper(VoidFunction fn, int recursive) {
    return disableDynamo(fn, recursive);
}

// Função principal para teste
void executeFunction() {
    printf("Executing function logic.\n");
}

int main() {
    // Exemplo de uso
    VoidFunction function = executeFunction;

    // Usar o disableDynamoWrapper similar ao decorador em Python
    VoidFunction disabledFunction = disableDynamoWrapper(function, 1);
    disabledFunction(); // Desativa e executa a função

    return 0;
}
