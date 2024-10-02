#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_PLANS 100
#define MAX_INVESTMENTS 100
#define MAX_ADDRESS_LENGTH 50
#define MAX_NAME_LENGTH 30

// Estrutura para o plano de investimento
typedef struct {
    char planName[MAX_NAME_LENGTH];
    int initialInvestment;
    int monthlyReturn;
    int annualReturn;
    int netAnnualReturn;
    int slots;
} InvestmentPlan;

// Estrutura para o investimento
typedef struct {
    char planName[MAX_NAME_LENGTH];
    int amount;
    char investorAddress[MAX_ADDRESS_LENGTH];
} Investment;

// Estrutura para o mecanismo Hollow
typedef struct {
    InvestmentPlan plans[MAX_PLANS];
    Investment investments[MAX_INVESTMENTS];
    int planCount;
    int investmentCount;
    char authenticationContractAddress[MAX_ADDRESS_LENGTH];
} HollowEngine;

// Função para inicializar o mecanismo Hollow
void initializeHollowEngine(HollowEngine* engine, const char* authenticationAddress) {
    strncpy(engine->authenticationContractAddress, authenticationAddress, MAX_ADDRESS_LENGTH);
    engine->planCount = 0;
    engine->investmentCount = 0;
}

// Função para adicionar um plano de investimento
void addPlan(HollowEngine* engine, const char* planName, int initialInvestment, int monthlyReturn, int annualReturn, int netAnnualReturn, int slots) {
    if (engine->planCount >= MAX_PLANS) {
        printf("Error: Maximum number of investment plans reached.\n");
        return;
    }
    InvestmentPlan* plan = &engine->plans[engine->planCount++];
    strncpy(plan->planName, planName, MAX_NAME_LENGTH);
    plan->initialInvestment = initialInvestment;
    plan->monthlyReturn = monthlyReturn;
    plan->annualReturn = annualReturn;
    plan->netAnnualReturn = netAnnualReturn;
    plan->slots = slots;
    printf("Plan '%s' added successfully!\n", planName);
}

// Função para investir em um plano
void invest(HollowEngine* engine, const char* planName, int amount, const char* investorAddress) {
    for (int i = 0; i < engine->planCount; i++) {
        if (strcmp(engine->plans[i].planName, planName) == 0) {
            if (engine->investmentCount >= MAX_INVESTMENTS) {
                printf("Error: Maximum number of investments reached.\n");
                return;
            }
            Investment* investment = &engine->investments[engine->investmentCount++];
            strncpy(investment->planName, planName, MAX_NAME_LENGTH);
            investment->amount = amount;
            strncpy(investment->investorAddress, investorAddress, MAX_ADDRESS_LENGTH);
            printf("Investment of %d completed successfully in plan '%s'!\n", amount, planName);
            return;
        }
    }
    printf("Error: Investment plan not found: %s\n", planName);
}

// Função para autenticar uma mensagem
void authenticateMessage(const char* messageHash) {
    // Simula a lógica de autenticação da mensagem
    printf("Message '%s' authenticated successfully!\n", messageHash);
}

int main() {
    HollowEngine engine;
    initializeHollowEngine(&engine, "someAuthenticationAddress");

    addPlan(&engine, "Plan A", 1000, 100, 1200, 1100, 5);
    addPlan(&engine, "Plan B", 2000, 200, 2400, 2200, 10);

    invest(&engine, "Plan A", 500, "Investor1Address");
    invest(&engine, "Plan C", 1500, "Investor2Address"); // Deve mostrar um erro

    authenticateMessage("someMessageHash");

    return 0;
}
