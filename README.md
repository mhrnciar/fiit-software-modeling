# Software modeling

## Set up dependency (IntelliJ IDEA)

1. Right-click on root folder and select `Open Module Settings`

2. Select `dependencies`
   
3. Select `+` -> `Library` -> `Java`
   
4. Navigate to JDBC SQLite driver
   - download [here](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc)
   - use jar in `dependencies` folder
    
5. Apply changes

6. In `Charity.iml` should appear `<orderEntry type="library" name="sqlite-jdbc-3.36.0.3" level="project" />` or 
   whatever version was downloaded and added to project
   