package com.example.todo_finalapp.Database;


public class repoUser {

    DaoUser dao;

    public repoUser(AppDatabase appDatabase) {
        dao = appDatabase.DaoUser();
    }

    public int getUser(String email, String password) {
        User user = dao.getUser(email, password);
        if (user != null) {
            return user.getId();
        }
        return 0;
    }
    public void insertUser(final User task){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(task);
            }
        });
    }
}