

# Setting up

## Prerequistes
* a dokku server with the following plugins
    * postgresql (https://github.com/dokku/dokku-postgres)
    * http-auth (https://github.com/dokku/dokku-http-auth)
    * letsencrypt (https://github.com/dokku/dokku-letsencrypt.git)  
* a dns A entry pointing to the dokku server

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

5. On your local machine push to dokku
```bash

# Add the dokku url to the downloaded project
git remote add dokku dokku@art-aimer.ch:bookstore

# push changes
git push dokku main
```

6. Change the domains to have that
```bash
dokku domains:report bookstore
=====> bookstore domains information
       Domains app enabled:           true                     
       Domains app vhosts:            stock.art-aimer.ch   
       ....
```

7. Activate lets'encrypt
```bash
dokku letsencrypt:enable bookstore
```

8. Enable basic authentication
```bash
dokku http-auth:on bookstore <username> <password>
```
