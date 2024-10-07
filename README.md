# Boggle

Le jeu Boggle développé par Catalin, Fafa, Samuel, Aranniy et Lorraine

Pour lancer la version graphique du jeu :

**Commandes :**

Commandes à utiliser pour effectuer l'installation de gradle :
-     sudo apt gradle

Commandes à utiliser pour effectuer le lancement du logiciel :

-     cd project-server/

-     ./gradlew bootrun

Une fois le serveur lancée, dans un autre terminal :

-     cd project-boggle/

-     ./gradlew bootrun

Pour lancer la version textuelle du jeu, il faudra exécuter le fichier Main.java depuis un IDE. 

**Attention :**

Il est possible que vous rencontriez des soucis lors du lancement du serveur.
Il est nécessaire que le **PORT 80** ne soit pas déjà utilisé !

Pour que le serveur soit utilisable depuis différentes machines, il vous faut modifier le fichier "project-boggle/src/main/resources/application.properties"
, il faut commenter la deuxième ligne à l'aide de "#" et décommenter la deuxième ligne et remplacer l'ip, par votre ip.
En faisant bien attention a ce que la ligne ce termine par :80, comme dans l'exemple ci-dessous : 
-   "http://votreIP:80"