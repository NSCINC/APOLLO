#include <stdio.h>
#include <stdlib.h>
#include <sqlite3.h>
#include <time.h>

// Função para criar tabelas no banco de dados
void createTables(sqlite3 *db) {
    char *errMsg = 0;
    const char *sqlUsers = "CREATE TABLE IF NOT EXISTS Users ("
                           "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                           "name TEXT NOT NULL,"
                           "email TEXT NOT NULL,"
                           "token TEXT NOT NULL);";

    const char *sqlCrmData = "CREATE TABLE IF NOT EXISTS CrmData ("
                              "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                              "user_id INTEGER,"
                              "data TEXT,"
                              "created_at DATETIME DEFAULT CURRENT_TIMESTAMP,"
                              "FOREIGN KEY(user_id) REFERENCES Users(id));";

    const char *sqlErpData = "CREATE TABLE IF NOT EXISTS ErpData ("
                              "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                              "user_id INTEGER,"
                              "data TEXT,"
                              "created_at DATETIME DEFAULT CURRENT_TIMESTAMP,"
                              "FOREIGN KEY(user_id) REFERENCES Users(id));";

    // Executa as instruções SQL
    if (sqlite3_exec(db, sqlUsers, 0, 0, &errMsg) != SQLITE_OK) {
        fprintf(stderr, "SQL error: %s\n", errMsg);
        sqlite3_free(errMsg);
    }
    
    if (sqlite3_exec(db, sqlCrmData, 0, 0, &errMsg) != SQLITE_OK) {
        fprintf(stderr, "SQL error: %s\n", errMsg);
        sqlite3_free(errMsg);
    }

    if (sqlite3_exec(db, sqlErpData, 0, 0, &errMsg) != SQLITE_OK) {
        fprintf(stderr, "SQL error: %s\n", errMsg);
        sqlite3_free(errMsg);
    }
}

int main() {
    sqlite3 *db;
    int exit;

    // Abre o banco de dados
    exit = sqlite3_open("iron_whale.db", &db);
    if (exit) {
        fprintf(stderr, "Can't open database: %s\n", sqlite3_errmsg(db));
        return exit;
    } else {
        fprintf(stderr, "Opened database successfully\n");
    }

    // Cria as tabelas
    createTables(db);

    // Fecha o banco de dados
    sqlite3_close(db);
    return 0;
}
