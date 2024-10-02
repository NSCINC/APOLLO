#include <stdio.h>
#include <string.h>

#define MAX_INVESTORS 100
#define HASH_SIZE 256

typedef struct {
    long initialInvestment;
    long monthlyReturn;
    long annualReturn;
    long netAnnualReturn;
    long slots;
} InvestmentPlan;

typedef struct {
    char investors[MAX_INVESTORS][50]; // Array de investidores
    long balances[MAX_INVESTORS];
    long investedAmount[MAX_INVESTORS];
    int authorizedInvestors[MAX_INVESTORS]; // 0: não autorizado, 1: autorizado
    int investorCount;
    char owner[50];
} InvestmentContract;

typedef struct {
    char owner[50];
    char messageHashes[MAX_INVESTORS][HASH_SIZE]; // Armazenar hashes de mensagens
    int isAuthenticated[MAX_INVESTORS]; // Autenticidade da mensagem
    int hashCount;
} AuthenticationContract;

typedef struct {
    InvestmentPlan investmentPlans[100];
    char planNames[100][50];
    int planCount;
    InvestmentContract *investmentContract;
    AuthenticationContract *authenticationContract;
} InvestmentManager;

// Funções para InvestmentContract
void invest(InvestmentContract *contract, const char *investor, long amount) {
    int investorIndex = -1;

    for (int i = 0; i < contract->investorCount; i++) {
        if (strcmp(contract->investors[i], investor) == 0) {
            investorIndex = i;
            break;
        }
    }

    if (investorIndex == -1 || !contract->authorizedInvestors[investorIndex]) {
        printf("Investor is not authorized\n");
        return;
    }

    if (amount <= 0) {
        printf("Investment amount must be greater than zero\n");
        return;
    }

    if (amount > contract->balances[investorIndex]) {
        printf("Insufficient balance\n");
        return;
    }

    contract->balances[investorIndex] -= amount;
    contract->investedAmount[investorIndex] += amount;
    printf("Investment of %ld made by %s\n", amount, investor);
}

void authorizeInvestor(InvestmentContract *contract, const char *investor, int authorized) {
    if (strcmp(contract->owner, investor) != 0) {
        printf("Only owner can perform this action\n");
        return;
    }

    for (int i = 0; i < contract->investorCount; i++) {
        if (strcmp(contract->investors[i], investor) == 0) {
            contract->authorizedInvestors[i] = authorized;
            printf("Authorization changed for %s: %d\n", investor, authorized);
            return;
        }
    }
}

long balanceOf(InvestmentContract *contract, const char *investor) {
    for (int i = 0; i < contract->investorCount; i++) {
        if (strcmp(contract->investors[i], investor) == 0) {
            return contract->balances[i];
        }
    }
    return 0;
}

long investedAmountOf(InvestmentContract *contract, const char *investor) {
    for (int i = 0; i < contract->investorCount; i++) {
        if (strcmp(contract->investors[i], investor) == 0) {
            return contract->investedAmount[i];
        }
    }
    return 0;
}

// Funções para AuthenticationContract
void authenticateMessage(AuthenticationContract *auth, const char *investor, const char *messageHash) {
    if (strcmp(auth->owner, investor) != 0) {
        printf("Only owner can perform this action\n");
        return;
    }

    for (int i = 0; i < auth->hashCount; i++) {
        if (strcmp(auth->messageHashes[i], messageHash) == 0 && auth->isAuthenticated[i]) {
            printf("Message already authenticated\n");
            return;
        }
    }

    strcpy(auth->messageHashes[auth->hashCount], messageHash);
    auth->isAuthenticated[auth->hashCount] = 1; // Autenticado
    auth->hashCount++;
    printf("Message authenticated: %s\n", messageHash);
}

int isMessageAuthenticated(AuthenticationContract *auth, const char *messageHash) {
    for (int i = 0; i < auth->hashCount; i++) {
        if (strcmp(auth->messageHashes[i], messageHash) == 0 && auth->isAuthenticated[i]) {
            return 1; // Verdadeiro
        }
    }
    return 0; // Falso
}

// Funções para InvestmentManager
void addPlan(InvestmentManager *manager, const char *planName, long initialInvestment, long monthlyReturn, long annualReturn, long netAnnualReturn, long slots) {
    InvestmentPlan plan = {initialInvestment, monthlyReturn, annualReturn, netAnnualReturn, slots};
    strcpy(manager->planNames[manager->planCount], planName);
    manager->investmentPlans[manager->planCount] = plan;
    manager->planCount++;
    printf("Plan added: %s\n", planName);
}

void investInPlan(InvestmentManager *manager, const char *planName, long amount, const char *investor) {
    invest(manager->investmentContract, investor, amount);
}

long getInvestmentContractBalance(InvestmentManager *manager, const char *investor) {
    return balanceOf(manager->investmentContract, investor);
}

void authenticateMessageInManager(InvestmentManager *manager, const char *investor, const char *messageHash) {
    authenticateMessage(manager->authenticationContract, investor, messageHash);
}

int isMessageAuthenticatedInManager(InvestmentManager *manager, const char *messageHash) {
    return isMessageAuthenticated(manager->authenticationContract, messageHash);
}

// Função principal para teste
int main() {
    InvestmentContract investmentContract = {.owner = "owner123", .investorCount = 0};

    // Adicionando investidores
    strcpy(investmentContract.investors[0], "investor1");
    investmentContract.balances[0] = 10000;
    investmentContract.authorizedInvestors[0] = 1; // autorizado
    investmentContract.investorCount++;

    strcpy(investmentContract.investors[1], "investor2");
    investmentContract.balances[1] = 5000;
    investmentContract.authorizedInvestors[1] = 0; // não autorizado
    investmentContract.investorCount++;

    AuthenticationContract authenticationContract = {.owner = "owner123", .hashCount = 0};

    InvestmentManager investmentManager = {.investmentContract = &investmentContract, .authenticationContract = &authenticationContract, .planCount = 0};

    addPlan(&investmentManager, "Plan A", 1000, 100, 1200, 1100, 10);
    investInPlan(&investmentManager, "Plan A", 500, "investor1");
    printf("Investor1 balance: %ld\n", getInvestmentContractBalance(&investmentManager, "investor1"));

    authenticateMessageInManager(&investmentManager, "owner123", "message_hash_1");
    printf("Is message authenticated: %d\n", isMessageAuthenticatedInManager(&investmentManager, "message_hash_1"));

    return 0;
}
