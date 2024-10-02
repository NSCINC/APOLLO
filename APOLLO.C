#include <stdio.h>
#include <stdlib.h>
#include <sqlite3.h>
#include <time.h>

// Função para criar tabelas no banco de dados
void createTables(sqlite3 *db) {
    char *errMsg = 0;
    
    // Tabela de Usuários
    const char *sqlUsers = "CREATE TABLE IF NOT EXISTS Users ("
                           "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                           "name TEXT NOT NULL,"
                           "email TEXT NOT NULL,"
                           "token TEXT NOT NULL);";

    // Tabela de Controle de Servidores
    const char *sqlServerData = "CREATE TABLE IF NOT EXISTS ServerData ("
                                "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                "user_id INTEGER,"
                                "server_name TEXT NOT NULL,"
                                "status TEXT NOT NULL,"
                                "last_checked DATETIME DEFAULT CURRENT_TIMESTAMP,"
                                "FOREIGN KEY(user_id) REFERENCES Users(id));";

    // Tabela de Logs do Data Center
    const char *sqlLogData = "CREATE TABLE IF NOT EXISTS LogData ("
                             "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                             "server_id INTEGER,"
                             "log_message TEXT,"
                             "log_time DATETIME DEFAULT CURRENT_TIMESTAMP,"
                             "FOREIGN KEY(server_id) REFERENCES ServerData(id));";

    // Executa as instruções SQL
    if (sqlite3_exec(db, sqlUsers, 0, 0, &errMsg) != SQLITE_OK) {
        fprintf(stderr, "SQL error: %s\n", errMsg);
        sqlite3_free(errMsg);
    }

    if (sqlite3_exec(db, sqlServerData, 0, 0, &errMsg) != SQLITE_OK) {
        fprintf(stderr, "SQL error: %s\n", errMsg);
        sqlite3_free(errMsg);
    }

    if (sqlite3_exec(db, sqlLogData, 0, 0, &errMsg) != SQLITE_OK) {
        fprintf(stderr, "SQL error: %s\n", errMsg);
        sqlite3_free(errMsg);
    }
}

int main() {
    sqlite3 *db;
    int exit;

    // Abre o banco de dados
    exit = sqlite3_open("apollo_holofi.db", &db);
    if (exit) {
        fprintf(stderr, "Can't open database: %s\n", sqlite3_errmsg(db));
        return exit;
    } else {
        fprintf(stderr, "Opened database successfully\n");
    }

    // Cria as tabelas do Data Center Controller
    createTables(db);

    // Fecha o banco de dados
    sqlite3_close(db);
    return 0;
}
