package base;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class BaseSetting, qui premet la connexion aux bases de données.
 *
 * @author Maxime Blaise
 */
public class BaseSetting {

    /**
     * BaseInformation, qui contient les informations lues dans le fichier.
     */
    private BaseInformation bi;

    /**
     * Objet connection.
     */
    private Connection connection;

    /**
     * Statement <=> Requête SQL.
     */
    private Statement statement;

    /**
     * ResultSet, lors des selects.
     */
    private ResultSet result_set;

    /**
     * Deuxième ResultSet
     */
    private ResultSet result_setBis;

    /**
     * Pour récupérer une seule instance.
     */
    private static final BaseSetting bs = new BaseSetting();

    /**
     * Constructeur vide
     */
    private BaseSetting() {

    }

    /**
     * Méthode qui se charge de lire les informations.
     *
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public final void lireInformation() throws IOException, ClassNotFoundException {
        // Lecture
        bi = BaseInformation.lectureInformations();
    }

    /**
     * Récupère une unique instance de l'objet
     *
     * @return BaseSetting
     */
    public static BaseSetting getInstance() {
        return bs;
    }

    /**
     * Méthode qui se charge des traitements lors d'une insertion.
     *
     * @param query requête à executer
     * @return vrai si la requête s'est bien exécutée.
     */
    public boolean insert(String query) {
        try {
            // Insertion.
            setStatement(getConnection().createStatement());
            getStatement().executeUpdate(query);

            // OK, bien exécutée.
            return true;
        } catch (SQLException ex) {
            // Si mode verbeux, on affiche un message d'erreur
            System.out.println("SQLException pour : " + query);

        }

        // NON OK, pas bien exécutée.
        return false;
    }

    /**
     * Méthode qui se charge des traitements lors d'une sélection.
     *
     * @param query requête à executer
     * @return vrai si la requête s'est bien exécutée.
     */
    public boolean select(String query) {
        try {
            // Sélection
            this.setStatement(getConnection().createStatement());
            this.setResult_set(this.getStatement().executeQuery(query));

            // OK, bien exécutée
            return true;
        } catch (SQLException ex) {
            // Si mode verbeux, on affiche un message d'erreur
            System.out.println("SQLException pour : " + query);

        }

        // NON OK, pas bien exécuté
        return false;
    }

    /**
     * Méthode qui se charge des traitements lors d'une sélection.
     *
     * @param query requête à executer
     * @return vrai si la requête s'est bien exécutée.
     */
    public boolean selectBis(String query) {
        try {
            // Sélection
            this.setStatement(getConnection().createStatement());
            this.setResult_setBis(this.getStatement().executeQuery(query));

            // OK, bien exécutée
            return true;
        } catch (SQLException ex) {
            // Si mode verbeux, on affiche un message d'erreur
            System.out.println("SQLException pour : " + query);
        }

        // NON OK, pas bien exécuté
        return false;
    }

    /**
     * Méthode qui test la connexion à la base, en initialisant l'objet
     * Connection
     *
     * @return true/false
     */
    public final boolean testerConnexion() {
        if (bi == null) {
            return false;
        }

        try {

            Class.forName(bi.getDriver());

            //Création du path
            String dbPath = String.format(
                    "jdbc:%s:%s/%s?user=%s&password=%s&characterEncoding=utf-8&"
                    + "useUnicode=true", bi.getDriver(), bi.getUrl(), bi.getDbname(), bi.getLogin(), bi.getPassword());

            //Initiatialisation de la connection
            connection = java.sql.DriverManager.getConnection(dbPath);

            // Préparation des tables pour l'UTF8
            prepareTables();

            //Si on arrive ici, c'est que tout s'est bien passé.
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            // Affichage d'un message d'erreur.
            Affichage.afficherMessageErreur("La connexion à la base de données a échoué.");
        }

        //Erreur quelque part !
        return false;
    }

    /**
     * Préparation des tables, encodage en UTF-8
     *
     * @throws SQLException
     */
    private void prepareTables() throws SQLException {
        java.sql.Statement stat = connection.createStatement();

        String myquery = "set names utf8";
        stat.execute(myquery);

        myquery = "set character set utf8";
        stat.execute(myquery);
    }

    /**
     * Récupère l'objet BaseInformation, qui contient toutes les informations
     * (juste les valeurs)
     *
     * @return les informations utiles
     */
    public BaseInformation getBaseInformations() {
        return this.bi;
    }


    /**
     * Récupère l'objet BaseInformation, qui contient toutes les informations
     * (juste les valeurs)
     *
     * @return les informations utiles
     */
    public BaseInformation getBi() {
        return bi;
    }

    /**
     * Permet de récupérer la connexion à la base.
     *
     * @return l'objet connexion
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Permet de réinitialiser la connection.
     *
     * @param connection l'objet connexion
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Récupère le statement courant.
     *
     * @return l'objet Statement
     */
    public Statement getStatement() {
        return statement;
    }

    /**
     * Initialiser la requête.
     *
     * @param statement l'objet Statement
     */
    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    /**
     * Récupérer le résultat d'une requête select.
     *
     * @return l'objet ResultSet
     */
    public ResultSet getResult_set() {
        return result_set;
    }

    /**
     * Initialise le résultat.
     *
     * @param result_set l'objet ResultSet
     */
    public void setResult_set(ResultSet result_set) {
        this.result_set = result_set;
    }

    public ResultSet getResult_setBis() {
        return result_setBis;
    }

    public void setResult_setBis(ResultSet result_setBis) {
        this.result_setBis = result_setBis;
    }

}
