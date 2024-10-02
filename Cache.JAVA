import java.util.ArrayList;

class InvestmentPlan {
    String planName;
    int initialInvestment;
    int monthlyReturn;
    int annualReturn;
    int netAnnualReturn;
    int slots;

    public InvestmentPlan(String planName, int initialInvestment, int monthlyReturn, int annualReturn, int netAnnualReturn, int slots) {
        this.planName = planName;
        this.initialInvestment = initialInvestment;
        this.monthlyReturn = monthlyReturn;
        this.annualReturn = annualReturn;
        this.netAnnualReturn = netAnnualReturn;
        this.slots = slots;
    }
}

class InvestmentPlanManager {
    private final ArrayList<InvestmentPlan> plans;

    public InvestmentPlanManager() {
        this.plans = new ArrayList<>();
    }

    // Função para adicionar um plano de investimento ao cache
    public void addInvestmentPlan(String planName, int initialInvestment, int monthlyReturn, int annualReturn, int netAnnualReturn, int slots) {
        System.out.println("Adding plan: " + planName);
        if (findPlanIndex(planName) != -1) {
            System.out.println("Error: Plan already exists");
            return;
        }
        plans.add(new InvestmentPlan(planName, initialInvestment, monthlyReturn, annualReturn, netAnnualReturn, slots));
        System.out.println("Plan added: " + planName);
    }

    // Função para buscar um plano de investimento no cache
    private int findPlanIndex(String planName) {
        for (int i = 0; i < plans.size(); i++) {
            if (plans.get(i).planName.equals(planName)) {
                return i;
            }
        }
        return -1; // Plano não encontrado
    }

    // Função para atualizar um plano de investimento no cache
    public void updateInvestmentPlan(String planName, Integer initialInvestment, Integer monthlyReturn, Integer annualReturn, Integer netAnnualReturn, Integer slots) {
        System.out.println("Updating plan: " + planName);
        int index = findPlanIndex(planName);
        if (index == -1) {
            System.out.println("Error: Plan does not exist");
            return;
        }
        InvestmentPlan plan = plans.get(index);
        if (initialInvestment != null) plan.initialInvestment = initialInvestment;
        if (monthlyReturn != null) plan.monthlyReturn = monthlyReturn;
        if (annualReturn != null) plan.annualReturn = annualReturn;
        if (netAnnualReturn != null) plan.netAnnualReturn = netAnnualReturn;
        if (slots != null) plan.slots = slots;
        System.out.println("Plan updated: " + planName);
    }

    // Função para remover um plano de investimento do cache
    public void removeInvestmentPlan(String planName) {
        System.out.println("Removing plan: " + planName);
        int index = findPlanIndex(planName);
        if (index == -1) {
            System.out.println("Error: Plan does not exist");
            return;
        }
        plans.remove(index);
        System.out.println("Plan removed: " + planName);
    }

    // Função para consultar os detalhes de um plano de investimento no cache
    public InvestmentPlan getInvestmentPlanDetails(String planName) {
        System.out.println("Fetching details for plan: " + planName);
        int index = findPlanIndex(planName);
        if (index == -1) {
            System.out.println("Error: Plan does not exist");
            return null;
        }
        return plans.get(index);
    }

    // Função para listar todos os planos de investimento
    public void listInvestmentPlans() {
        System.out.println("Listing all investment plans:");
        for (InvestmentPlan plan : plans) {
            System.out.println("Plan Name: " + plan.planName);
            System.out.println("  Initial Investment: " + plan.initialInvestment);
            System.out.println("  Monthly Return: " + plan.monthlyReturn);
            System.out.println("  Annual Return: " + plan.annualReturn);
            System.out.println("  Net Annual Return: " + plan.netAnnualReturn);
            System.out.println("  Slots: " + plan.slots + "\n");
        }
    }
}

public class InvestmentPlanApp {
    public static void main(String[] args) {
        InvestmentPlanManager manager = new InvestmentPlanManager();

        // Etapa 1: Adicionar um plano de investimento
        System.out.println("\nStep 1: Adding an Investment Plan");
        manager.addInvestmentPlan("economicPlan", 500, 5, 60, 300, 500);

        // Etapa 2: Consultar detalhes de um plano de investimento
        System.out.println("\nStep 2: Fetching Investment Plan Details");
        InvestmentPlan plan = manager.getInvestmentPlanDetails("economicPlan");
        if (plan != null) {
            System.out.println("Initial Investment: " + plan.initialInvestment);
            System.out.println("Monthly Return: " + plan.monthlyReturn);
            System.out.println("Annual Return: " + plan.annualReturn);
            System.out.println("Net Annual Return: " + plan.netAnnualReturn);
            System.out.println("Slots: " + plan.slots);
        }

        // Etapa 3: Atualizar um plano de investimento
        System.out.println("\nStep 3: Updating an Investment Plan");
        int newMonthlyReturn = 10;
        int newNetAnnualReturn = 400;
        manager.updateInvestmentPlan("economicPlan", null, newMonthlyReturn, null, newNetAnnualReturn, null);

        // Etapa 4: Consultar detalhes do plano de investimento após atualização
        System.out.println("\nStep 4: Fetching Updated Investment Plan Details");
        plan = manager.getInvestmentPlanDetails("economicPlan");
        if (plan != null) {
            System.out.println("Initial Investment: " + plan.initialInvestment);
            System.out.println("Monthly Return: " + plan.monthlyReturn);
            System.out.println("Annual Return: " + plan.annualReturn);
            System.out.println("Net Annual Return: " + plan.netAnnualReturn);
            System.out.println("Slots: " + plan.slots);
        }

        // Etapa 5: Listar todos os planos de investimento
        System.out.println("\nStep 5: Listing All Investment Plans");
        manager.listInvestmentPlans();

        // Etapa 6: Remover um plano de investimento
        System.out.println("\nStep 6: Removing an Investment Plan");
        manager.removeInvestmentPlan("economicPlan");

        // Etapa 7: Listar todos os planos de investimento após remoção
        System.out.println("\nStep 7: Listing All Investment Plans After Removal");
        manager.listInvestmentPlans();
    }
}
