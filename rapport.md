# Rapport du second rendu (rendu2)

## Nouvelles implémentations depuis le premier rendu

Nous avons complété la vue afin qu'elle soit fonctionnelle :

- Ajout des différents boutons et de leurs connexions avec GameController ; 
- Ajout de l'objectif du joueur acutel ;
- Ajout de la position des joueurs et des objectifs en superposition des images.

Nous avons ajouté la possibilité que certaines tuiles aient un objectif et
nous avons fait en sorte qu'un joueur ne sorte pas du plateau mais se retrouve de l'autre côté quand 
une tuile est inséré. De plus le joueur bouge même temps que la ligne ou la colonne sur laquelle il est.

## Explications d'implémentations

Il existe une surchage de updateGameStatus car pour afficher le gagnant il faut le passer en paramètre de l'observer. Cependant, un changement de statut n'indique par forcément une fin de partie donc il n'y a aucune raison de passer un gagnant en paramètre si le changement de statut n'indique pas une fin de partie. De plus une fin de partie peut se faire sans gagnant s'il y a abandon.

Actuellement le main initialise le modèle avec game.init(). Dans une version future ce serait à la vue de le proposer via un bouton start. Cependant, l'implémentation actuelle de la vue ne le permet pas mais la méthode du controller existe au cas où quelqu'un veuille le faire

Il y a dans PlayerManagment un tableau de quatre éléments qui représente les quatre joueurs. Pour connaître le joueur actuel il y a une variable qui stocke l'index du joueur courant. Pour obtenir la position du joueur il suffit de récupérer du tableau le joueur actuel et d'utiliser un dictionnaire qui a pour clé valeur un joueur et un Vector2D qui représente la position du joueur.

Une rotation d'une tuile correspond à changer les directions que la tuile autorise

## Changement de conception depuis le premier rendu

- La méthode d'attribution d'un objectif à une tuile

- Ajout de différentes méthodes dans l'interface Observer :
  - pour observer le joueur actuel pour afficher son objectif à atteindre ;
  - pour observer le statut actuel du jeu ;
  - pour observer les directions possibles du joueur acutel.
- Modification de la méthode updateBoard de l'interface Observer pour permettre la superposition des tuiles et des joueurs qui sont dessus, on passe donc en paramètres le plateau et la position des joueurs.
- Division de la classe Board en classe Board, Game et PlayerManagment pour que chaque classe ait sa responsabilité même si Game a plus de responsabilité que les autres car il fait office de façade.
- Ajout d'un nom aux joueurs car il est plus facile d'utiliser getName qu'un index pour différencier les joueurs dans la vue. Il s'avère que le nom d'un joueur correspond à sa couleur pour faire le plus simple possible.
- Modification en conséquence du constructeur en créant dans PlayerManagement un tableau des noms.
- Avant Player avait un nombre d'objectifs rempli et à remplir, on a changé le à remplir par le nombre maximum d'objectifs qu'il doit atteindre afin d'améliorer la compréhension de l'affichage. Renommer les méthodes est facile sur InteliJ grâce à l'outil refactor.
- Suppression des méthodes qui faisaient doublon dans Tile et dans Player.
- Ajout de possibleDirection dans Game pour indiquer à la vue quelle flèche de directions bloquer, l'attribut dépend du joueur actuel et s'il a déjà inséré une tuile ou non.

## difficultés rencontrées

- Les déplacements des joueurs étaient faux, ils pouvaient se déplacer sur des cases adjacentes sans que les tuiles ne soient reliées. C'était un problème dans le modèle. Créer la vue nous a permis de nous rendre compte des erreurs faites dans le modèle, il manquait des vérifications pour le déplacement et les axes étaient inversés.
- Problème de la taille de la fenêtre, nous avons donc mis une taille minimale en largeur et en hauteur afin d'éviter les aberrations d'affichage.

## Améliorations possibles

- TileFactory pourrait aussi gérer la rotation aléatoire d'une tuile.
- Ajouter une énumération pour le nom des joueurs et assigner à chaque nom une couleur.
- Améliorer le système de vérification de quelle ligne ou colonne est déplaçable.
- Améliorer le système qui force le joueur à insérer une tuile au début de son tour, avant de se déplacer.
- Clarifier l'utilisation des axes X et Y.
- Encore diviser Game en différentes sous-classes.
- Supprimer la possibilité à GameController de gérer le fait de ne pouvoir insérer une tuile qu'une seule fois durant son tour.

## Eléments non-implémentés

- Le fait d'interdire au joueur suivant le mouvement inverse de celui qui vient d'être réalisé (niveau 4 de la fonctionnalité 6).
- L'écran de fin qui propose de recommencer une partie.

## Amélioration possible pour les prochains  projets

- Ne pas attendre d'avoir la vue en place pour tester le modèle et donc prévoir les tests en amont de l'implémentation.
- Ne pas hésiter à modifier son code en cours de route quand on se rend compte qu'il y a moyen de faire mieux.
