import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

class InvestmentRequest {
    String token;
    String planName;
    int initialInvestment;
    int amount;
    String investorAddress;

    public InvestmentRequest(String token, String planName, int initialInvestment, int amount, String investorAddress) {
        this.token = token;
        this.planName = planName;
        this.initialInvestment = initialInvestment;
        this.amount = amount;
        this.investorAddress = investorAddress;
    }
}

class SimpleNeuralNetwork {
    private static final int MAX_WEIGHTS = 3;
    private double[] weights;

    public SimpleNeuralNetwork() {
        weights = new double[MAX_WEIGHTS];
        Random random = new Random();
        for (int i = 0; i < MAX_WEIGHTS; i++) {
            weights[i] = random.nextDouble();
        }
    }

    public double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public double feedforward(double[] inputData) {
        double weightedSum = 0.0;
        for (int i = 0; i < MAX_WEIGHTS; i++) {
            weightedSum += inputData[i] * weights[i];
        }
        return sigmoid(weightedSum);
    }
}

public class InvestmentApp {
    private static final String VALID_TOKEN = "valid_token";

    public static void main(String[] args) {
        // Inicializa a rede neural
        SimpleNeuralNetwork neuralNetwork = new SimpleNeuralNetwork();
        
        double[] crmData = {500.0, 100.0, 5.0};
        double result = neuralNetwork.feedforward(crmData);
        System.out.printf("Resultado do processamento de CRM: %.2f%n", result);
        
        // Simulando um pedido de investimento
        InvestmentRequest request = new InvestmentRequest("valid_token", "Investment Plan A", 1000, 500, "0x123456789");
        addPlan(request);
        invest(request);
    }

    private static boolean validateNscToken(String token) {
        return VALID_TOKEN.equals(token);
    }

    private static void runLuaContract(String script, String function, String[] args) {
        StringBuilder command = new StringBuilder("lua " + script + " " + function);
        for (String arg : args) {
            command.append(" ").append(arg);
        }

        try {
            Process process = Runtime.getRuntime().exec(command.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.lines().reduce("", (accumulator, line) -> accumulator + line + "\n");
            System.out.print(output);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error executing Lua contract: " + e.getMessage());
        }
    }

    private static void addPlan(InvestmentRequest request) {
        if (!validateNscToken(request.token)) {
            System.out.println("Token NSC inválido");
            return;
        }

        String initialInvestmentStr = String.valueOf(request.initialInvestment);
        String[] args = {request.planName, initialInvestmentStr};
        runLuaContract("investment_contract.lua", "add_plan", args);
    }

    private static void invest(InvestmentRequest request) {
        String amountStr = String.valueOf(request.amount);
        String[] args = {request.planName, amountStr, request.investorAddress};
        runLuaContract("investment_contract.lua", "invest", args);
    }
}
