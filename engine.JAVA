import java.util.ArrayList;
import java.util.List;

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

class Investment {
    String planName;
    int amount;
    String investorAddress;

    public Investment(String planName, int amount, String investorAddress) {
        this.planName = planName;
        this.amount = amount;
        this.investorAddress = investorAddress;
    }
}

class HollowEngine {
    List<InvestmentPlan> plans;
    List<Investment> investments;
    String authenticationContractAddress;

    public HollowEngine(String authenticationAddress) {
        this.authenticationContractAddress = authenticationAddress;
        this.plans = new ArrayList<>();
        this.investments = new ArrayList<>();
    }

    // Method to add an investment plan
    public void addPlan(String planName, int initialInvestment, int monthlyReturn, int annualReturn, int netAnnualReturn, int slots) {
        InvestmentPlan plan = new InvestmentPlan(planName, initialInvestment, monthlyReturn, annualReturn, netAnnualReturn, slots);
        plans.add(plan);
        System.out.println("Plan '" + planName + "' added successfully!");
    }

    // Method to invest in a plan
    public void invest(String planName, int amount, String investorAddress) {
        for (InvestmentPlan plan : plans) {
            if (plan.planName.equals(planName)) {
                Investment investment = new Investment(planName, amount, investorAddress);
                investments.add(investment);
                System.out.println("Investment of " + amount + " completed successfully in plan '" + planName + "'!");
                return;
            }
        }
        System.out.println("Error: Investment plan not found: " + planName);
    }

    // Method to authenticate a message
    public void authenticateMessage(String messageHash) {
        // Simulate message authentication logic
        System.out.println("Message '" + messageHash + "' authenticated successfully!");
    }
}

public class Main {
    public static void main(String[] args) {
        HollowEngine engine = new HollowEngine("someAuthenticationAddress");

        engine.addPlan("Plan A", 1000, 100, 1200, 1100, 5);
        engine.addPlan("Plan B", 2000, 200, 2400, 2200, 10);

        engine.invest("Plan A", 500, "Investor1Address");
        engine.invest("Plan C", 1500, "Investor2Address"); // Should show an error

        engine.authenticateMessage("someMessageHash");
    }
}
