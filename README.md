

# Setting up

## Prerequistes
* a dokku server with the following plugins
    * postgresql (https://github.com/dokku/dokku-postgres)
    * http-auth (https://github.com/dokku/dokku-http-auth)
    
1. Connect to dokku server with ssh
2. Create the app
```bash
dokku apps:create bookstore
```

3. create a database
```bash
dokku postgres:create bookstore-db
```

4. link database to app
```bash
dokku postgres:link bookstore-db bookstore
```