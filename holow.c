import java.util.ArrayList;
import java.util.List;

// Classe para representar um plano de investimento no CRM/ERP
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

// Classe para representar um investimento no CRM/ERP
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

// Classe do sistema ERP/CRM (HollowEngine equivalente)
class HollowEngine {
    String investmentContractAddress;
    String authenticationContractAddress;
    List<InvestmentPlan> plans;
    List<Investment> investments;

    public HollowEngine(String investmentContractAddress, String authenticationContractAddress) {
        this.investmentContractAddress = investmentContractAddress;
        this.authenticationContractAddress = authenticationContractAddress;
        this.plans = new ArrayList<>();
        this.investments = new ArrayList<>();
    }

    // Método para adicionar um plano de investimento no CRM/ERP
    public void addPlan(String planName, int initialInvestment, int monthlyReturn, int annualReturn, int netAnnualReturn, int slots) {
        InvestmentPlan plan = new InvestmentPlan(planName, initialInvestment, monthlyReturn, annualReturn, netAnnualReturn, slots);
        plans.add(plan);
        System.out.printf("Plano '%s' adicionado com sucesso!%n", planName);
    }

    // Método para realizar um investimento em um plano de investimento
    public void invest(String planName, int amount, String investorAddress) {
        boolean planFound = false;
        for (InvestmentPlan plan : plans) {
            if (plan.planName.equals(planName)) {
                planFound = true;
                break;
            }
        }

        if (!planFound) {
            System.out.printf("Plano de investimento não encontrado: %s%n", planName);
            return;
        }

        Investment investment = new Investment(planName, amount, investorAddress);
        investments.add(investment);
        System.out.printf("Investimento de %d realizado com sucesso no plano '%s' pelo investidor '%s'.%n", amount, planName, investorAddress);
    }

    // Método para simular a autenticação de uma mensagem
    public void authenticateMessage(String messageHash) {
        System.out.printf("Mensagem autenticada com sucesso! Hash: %s%n", messageHash);
    }
}

public class Main {
    public static void main(String[] args) {
        // Endereços de exemplo para contratos simulados
        String investmentContractAddress = "0x1111111111111111111111111111111111111111";
        String authenticationContractAddress = "0x2222222222222222222222222222222222222222";

        // Instanciar e inicializar HollowEngine (ERP/CRM)
        HollowEngine engine = new HollowEngine(investmentContractAddress, authenticationContractAddress);

        // Adicionar um plano de investimento
        System.out.println("\nPasso 1: Adicionando um Plano de Investimento");
        engine.addPlan("economicPlan", 500, 5, 60, 300, 500);

        // Realizar um investimento em um plano
        System.out.println("\nPasso 2: Realizando um Investimento no Plano 'economicPlan'");
        engine.invest("economicPlan", 100, "0x3333333333333333333333333333333333333333");

        // Autenticar uma mensagem
        System.out.println("\nPasso 3: Autenticando uma Mensagem");
        String messageHash = "0xabcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890";
        engine.authenticateMessage(messageHash);

        // Fim dos testes
        System.out.println("\nTestes do kernel do ERP/CRM concluídos.");
    }
}
