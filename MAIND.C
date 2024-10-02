typedef struct {
    InvestmentPlan *plans; // Pointer to dynamically allocated array
    int count;
    int capacity; // Current allocated capacity
} InvestmentPlanManager;

// Function to initialize the manager with dynamic memory
void initInvestmentPlanManager(InvestmentPlanManager *manager) {
    manager->count = 0;
    manager->capacity = 10; // Initial capacity
    manager->plans = (InvestmentPlan *)malloc(manager->capacity * sizeof(InvestmentPlan));
}

// Modify addInvestmentPlan to handle dynamic resizing
void addInvestmentPlan(InvestmentPlanManager *manager, const char *planName, int initialInvestment, int monthlyReturn, int annualReturn, int netAnnualReturn, int slots) {
    if (manager->count >= manager->capacity) {
        manager->capacity *= 2; // Double the capacity
        manager->plans = (InvestmentPlan *)realloc(manager->plans, manager->capacity * sizeof(InvestmentPlan));
    }
    // The rest of the function remains the same...
}

// Remember to free the memory when done
free(manager.plans);
