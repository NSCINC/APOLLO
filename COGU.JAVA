import java.util.Arrays;

class InvestmentPlan {
    long initialInvestment;
    long monthlyReturn;
    long annualReturn;
    long netAnnualReturn;
    long slots;

    public InvestmentPlan(long initialInvestment, long monthlyReturn, long annualReturn, long netAnnualReturn, long slots) {
        this.initialInvestment = initialInvestment;
        this.monthlyReturn = monthlyReturn;
        this.annualReturn = annualReturn;
        this.netAnnualReturn = netAnnualReturn;
        this.slots = slots;
    }
}

class InvestmentContract {
    static final int MAX_INVESTORS = 100;
    String[] investors = new String[MAX_INVESTORS]; // Array de investidores
    long[] balances = new long[MAX_INVESTORS];
    long[] investedAmount = new long[MAX_INVESTORS];
    boolean[] authorizedInvestors = new boolean[MAX_INVESTORS];
    int investorCount = 0;
    String owner;

    public InvestmentContract(String owner) {
        this.owner = owner;
    }

    public void invest(String investor, long amount) {
        int investorIndex = findInvestorIndex(investor);

        // Verificar se o investidor está autorizado
        if (investorIndex == -1 || !authorizedInvestors[investorIndex]) {
            System.out.println("Investor is not authorized");
            return;
        }

        if (amount <= 0) {
            System.out.println("Investment amount must be greater than zero");
            return;
        }

        if (amount > balances[investorIndex]) {
            System.out.println("Insufficient balance");
            return;
        }

        balances[investorIndex] -= amount;
        investedAmount[investorIndex] += amount;
        System.out.printf("Investment of %d made by %s\n", amount, investor);
    }

    public void authorizeInvestor(String investor, boolean authorized) {
        if (!owner.equals(investor)) {
            System.out.println("Only owner can perform this action");
            return;
        }

        int index = findInvestorIndex(investor);
        if (index != -1) {
            authorizedInvestors[index] = authorized;
            System.out.printf("Authorization changed for %s: %b\n", investor, authorized);
        }
    }

    public long balanceOf(String investor) {
        int index = findInvestorIndex(investor);
        return (index != -1) ? balances[index] : 0;
    }

    public long investedAmountOf(String investor) {
        int index = findInvestorIndex(investor);
        return (index != -1) ? investedAmount[index] : 0;
    }

    private int findInvestorIndex(String investor) {
        for (int i = 0; i < investorCount; i++) {
            if (investors[i].equals(investor)) {
                return i;
            }
        }
        return -1;
    }
}

class AuthenticationContract {
    static final int HASH_SIZE = 256;
    String owner;
    String[] messageHashes = new String[InvestmentContract.MAX_INVESTORS]; // Armazenar hashes de mensagens
    boolean[] isAuthenticated = new boolean[InvestmentContract.MAX_INVESTORS]; // Autenticidade da mensagem
    int hashCount = 0;

    public AuthenticationContract(String owner) {
        this.owner = owner;
    }

    public void authenticateMessage(String investor, String messageHash) {
        if (!owner.equals(investor)) {
            System.out.println("Only owner can perform this action");
            return;
        }

        for (int i = 0; i < hashCount; i++) {
            if (messageHashes[i].equals(messageHash) && isAuthenticated[i]) {
                System.out.println("Message already authenticated");
                return;
            }
        }

        messageHashes[hashCount] = messageHash;
        isAuthenticated[hashCount] = true;
        hashCount++;
        System.out.printf("Message authenticated: %s\n", messageHash);
    }

    public boolean isMessageAuthenticated(String messageHash) {
        for (int i = 0; i < hashCount; i++) {
            if (messageHashes[i].equals(messageHash) && isAuthenticated[i]) {
                return true;
            }
        }
        return false;
    }
}

class InvestmentManager {
    InvestmentPlan[] investmentPlans = new InvestmentPlan[100];
    String[] planNames = new String[100];
    int planCount = 0;
    InvestmentContract investmentContract;
    AuthenticationContract authenticationContract;

    public InvestmentManager(InvestmentContract investmentContract, AuthenticationContract authenticationContract) {
        this.investmentContract = investmentContract;
        this.authenticationContract = authenticationContract;
    }

    public void addPlan(String planName, long initialInvestment, long monthlyReturn, long annualReturn, long netAnnualReturn, long slots) {
        InvestmentPlan plan = new InvestmentPlan(initialInvestment, monthlyReturn, annualReturn, netAnnualReturn, slots);
        planNames[planCount] = planName;
        investmentPlans[planCount] = plan;
        planCount++;
        System.out.printf("Plan added: %s\n", planName);
    }

    public void investInPlan(String planName, long amount, String investor) {
        investmentContract.invest(investor, amount);
    }

    public long getInvestmentContractBalance(String investor) {
        return investmentContract.balanceOf(investor);
    }

    public void authenticateMessageInManager(String investor, String messageHash) {
        authenticationContract.authenticateMessage(investor, messageHash);
    }

    public boolean isMessageAuthenticatedInManager(String messageHash) {
        return authenticationContract.isMessageAuthenticated(messageHash);
    }
}

// Função principal para teste
public class InvestmentSystem {
    public static void main(String[] args) {
        InvestmentContract investmentContract = new InvestmentContract("owner123");

        // Adicionando investidores
        investmentContract.investors[0] = "investor1";
        investmentContract.balances[0] = 10000;
        investmentContract.authorizedInvestors[0] = true;
        investmentContract.investorCount++;

        investmentContract.investors[1] = "investor2";
        investmentContract.balances[1] = 5000;
        investmentContract.authorizedInvestors[1] = false;
        investmentContract.investorCount++;

        AuthenticationContract authenticationContract = new AuthenticationContract("owner123");

        InvestmentManager investmentManager = new InvestmentManager(investmentContract, authenticationContract);

        investmentManager.addPlan("Plan A", 1000, 100, 1200, 1100, 10);
        investmentManager.investInPlan("Plan A", 500, "investor1");
        System.out.printf("Investor1 balance: %d\n", investmentManager.getInvestmentContractBalance("investor1"));

        investmentManager.authenticateMessageInManager("owner123", "message_hash_1");
        System.out.printf("Is message authenticated: %b\n", investmentManager.isMessageAuthenticatedInManager("message_hash_1"));
    }
}
