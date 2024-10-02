#include <stdio.h>
#include <stdlib.h>
#include <sqlite3.h>
#include <string.h>

// Estrutura para armazenar dados de servidores
typedef struct {
    char name[100];
    char status[100];
    char ownerEmail[100];
} Server;

// Função para abrir a conexão com o banco de dados
sqlite3* connectDatabase() {
    sqlite3 *db;
    int rc = sqlite3_open("apollo_holofi.db", &db);
    
    if (rc) {
        fprintf(stderr, "Erro ao abrir banco de dados: %s\n", sqlite3_errmsg(db));
        return NULL;
    } else {
        printf("Banco de dados aberto com sucesso.\n");
        return db;
    }
}

// Função para inicializar o banco de dados, criando a tabela Servers
void initializeDatabase(sqlite3 *db) {
    const char *sql = "CREATE TABLE IF NOT EXISTS Servers ("
                      "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                      "name TEXT NOT NULL,"
                      "status TEXT NOT NULL,"
                      "owner_email TEXT NOT NULL);";
    
    char *errMsg = 0;
    int rc = sqlite3_exec(db, sql, 0, 0, &errMsg);
    
    if (rc != SQLITE_OK) {
        fprintf(stderr, "Erro ao criar tabela: %s\n", errMsg);
        sqlite3_free(errMsg);
    } else {
        printf("Tabela Servers criada ou já existe.\n");
    }
}

// Função para inserir um servidor no banco de dados
void insertServer(sqlite3 *db, Server *server) {
    const char *sql = "INSERT INTO Servers(name, status, owner_email) VALUES(?, ?, ?);";
    sqlite3_stmt *stmt;

    if (sqlite3_prepare_v2(db, sql, -1, &stmt, NULL) != SQLITE_OK) {
        fprintf(stderr, "Falha ao preparar statement: %s\n", sqlite3_errmsg(db));
        return;
    }

    sqlite3_bind_text(stmt, 1, server->name, -1, SQLITE_STATIC);
    sqlite3_bind_text(stmt, 2, server->status, -1, SQLITE_STATIC);
    sqlite3_bind_text(stmt, 3, server->ownerEmail, -1, SQLITE_STATIC);

    if (sqlite3_step(stmt) != SQLITE_DONE) {
        fprintf(stderr, "Erro ao inserir servidor: %s\n", sqlite3_errmsg(db));
    } else {
        printf("Servidor %s inserido com sucesso.\n", server->name);
    }

    sqlite3_finalize(stmt);
}

// Função para ler dados de servidores a partir de um arquivo CSV
void readCsvData(sqlite3 *db, const char *filePath) {
    FILE *file = fopen(filePath, "r");
    if (!file) {
        fprintf(stderr, "Erro ao abrir o arquivo CSV.\n");
        return;
    }

    char line[256];
    while (fgets(line, sizeof(line), file)) {
        Server server;
        char *token = strtok(line, ",");

        // Nome do servidor
        strcpy(server.name, token);

        // Status do servidor
        token = strtok(NULL, ",");
        strcpy(server.status, token);

        // Email do dono do servidor
        token = strtok(NULL, ",");
        strcpy(server.ownerEmail, token);

        // Inserir servidor no banco de dados
        insertServer(db, &server);
    }

    fclose(file);
}

int main() {
    sqlite3 *db = connectDatabase();
    
    if (db == NULL) {
        return 1;
    }

    // Inicializa o banco de dados e cria a tabela Servers
    initializeDatabase(db);

    // Lê os dados do arquivo CSV e insere no banco de dados
    readCsvData(db, "servers_data.csv");

    // Fecha o banco de dados
    sqlite3_close(db);

    return 0;
}
