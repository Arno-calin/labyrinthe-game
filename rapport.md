# Rapport du premier rendu (rendu1)

## Répartition du travail

Après avoir conçu la base de notre projet et mis en place le patron de conception observé, nous nous sommes répartis le travail comme suit :

- Lilian s'occupe de créer la vue : swing, la gestion des layouts, la gestion des images, le lien avec le contrôleur.
- Arno s'occupe du modèle et du contrôleur.

### Lilian

J'ai d'abord établi le début de la vue, les boutons et le layout qui contient le plateau de jeu, j'ai opté pour un GridBagLayout pour pouvoir placer les éléments à des endroits précis plutôt qu'à la suite les uns des autres.
L'étape suivante à été de coder un algorithme permettant de déterminer la rotation à effectuer sur une image à partir de l'attribut ArrayList \<Direction\> d'un objet Tile donné en paramètre car on ne connaît pas sa rotation actuelle. L'algorithme se trouve dans la fonction getTileImage qui retourne l'image correspondant à l'objet Tile.
J'ai ensuite effectué plusieurs optimisations qui m'ont permis de réduire le temps de chargement de ce tableau d'environ 3 secondes à environ 0,5 secondes.

### Arno

La classe GameController gère le cycle de vie du modèle. Il crée les tuiles à l'aide de la factory pour s'assurer de la bonne forme de la tuile demandée et éviter d'écrire à la main le tableau de directions, mais juste demander la forme que l'on veut, cela évite les risques d'erreurs. Il crée aussi les quatre joueurs et distribue aléatoirement leurs objectifs.  
Un joueur et une tuile ne connaissent pas l'environnement dans lequel ils évoluent, seul Board le sait.

## Choix de conception

Nous avons commencé par concevoir le modèle puis nous avons conçu tout autour le patron de conception _observer_ pour créer la vue et écrire les premières méthodes du controller. Après nous avons écrit les quelques lignes du _main_ qui ne devraient plus changer.

Nous avons décidé de mettre la fonction _main_ en dehors du package _app_ car elle ne fait pas partie du fonctionnement de l'application et sert seulement à instancier les différents éléments.

### Difficultés rencontrées et solutions apportées

- Pour représenter la position du joueur nous avons utilisé un tableau de deux éléments x et y cependant cette strcuture ressemble beaucoup à un vecteur deux dimensions. Nous avons donc créé une classe Vector2D pour représenter la position d'un joueur. Cela permet de mieux manipuler les données.
- Au départ nous étions parti sur un tableau de booléen pour représenter la forme de la tuile après conseil du professeur nous l'avons transformé en ArrayList de Direction qui est énumération des quatre directions possibles. Cela est plus facile à gérer et respecte le paradigme objet.
- Pour insérer une tuile dans le jeu, il fallait utiliser l'une des quatre méthodes qui le permettaient. Après développement, il s'est avéré qu'elles avaient le même code. Nous avons donc ressembler le code identique dans une cinquième fonction qui est la seul public qui prend en paramètre une direction pour déterminer quelle sous-méthode utiliser.
- La dernière tuile, celle que l'on peut insérer dans le plateau, appartient à la classe Board et non au controller car la vue n'observe que le modèle.
- La classe Board génère une erreur si on essaye d'insérer une tuile dans une ligne ou une colonne fixe, le controller doit s'assurer de ne pas lever cette erreur (ce mécanisme pourra évoluer avec la modification de la vue).
- La classe Board possède un dictionnaire de clé Player et de valeur Vector2D, pour l'uml nous avons fait comme il nous semblait pertinent.
- La méthode _initGame_ existe afin que la vue observe le modèle avant que celui-ci ne s'initialise, pour éviter que la vue n'affiche rien au démarrage.
- Le plateau ne s'affiche pas correctement sur Linux, cela peut être lié au redimensionnement forcé de la fenêtre.
- Actuellement, la tuile ne connaît pas son image même si un attribut lui est dédié, nous utilisons pour l'instant un algorithme qui fait l'association entre Tile et l'image en tenant compte de l'orientation. Ce fonctionnement pourrait être amené à évoluer s'il s'avère inefficace, mais s'il fonctionne, on ne le modifiera qu'au moment de l'optimisation.

### Conceptions manquantes

- Nous n'avons pas encore réfléchit à comment éviter qu'un joueur ne refasse l'action précédente. (un attribut dans Board certainement et/ou un bouton désactivé dans la vue).
- Les tuiles qui restent fixent d'une partie à une autre (nous pensons ajouter dans la méthode init de Board la création de tuiles de rotation et de forme et d'objectif prédéfinis).

## Implémentations déjà effectuées

Nous avons fini d'implémenter le modèle et le controller.

Il ne manque plus que la vue qui est en cours de programmation car cette dernière est la plus longue à implémenter et à faire fonctionner.
Il est tout à fait possible que les méthodes actuelles du controller changent pour être mieux adapter à la vue.
Le modèle n'a aucune raison de changer sauf si nous rencontrons de trop grandes difficultés ou s'il faut rajouter des méthodes auxquelles nous n'avons pas encore pensées.

Les images ont été faites via photoshop.

## Prochaines étapes

- Ecrire des tests unitaires qui permettront de s'assurer du bon fonctionneement du modèle indépendemment de la vue.
- Finir rapidement la vue pour tester le jeu via l'interface graphique et ainsi améliorer le projet.
- Créer l'écran de fin et donc la gestion de fin de partie (pour l'instant le programme quitte brutalement).



# Rapport du second rendu (rendu2)

## Lilian
Pendant la partie 2, j'ai complété la vue, j'ai ajouté les boutons de déplacement, de rotation et de fin de tour.
J'ai aussi ajouté les labels qui indiquent le joueur actif et les objectifs des joueurs.
J'ai du a plusieurs reprises régler des problemes d'affichage des tuiles.
J'ai ensuite implémenté l'afficahge d'un joueur qui se fait via un pion.
L'étape suivante à été de gérer les déplacements des joueurs, Arno s'est occupé de la partie logique et j'ai géré l'affichage.
Nous avons du modifier plusieurs fois les fichiers du modèle pour s'adapter à la vue. Je pense que je fait d'avoir fait les modèles avant la vue nous a permis de gagner du temps. Cependant, nous avons surtout du modifier les valeurs passées en paramètre des fonctions de l'observer pour les rendre utilisables par la vue.
J'ai ensuite ajouté l'affichage des objectifs.
Pour cela, j'ai du ajouter la génération aléatoire de la position des objectifs sur le plateau.
Pour cela, j'ai du modifier la façon dont les objectifs sont affectés aux tuiles.

Par exemple :
    Nous avions d'abord une fonction updatePlayers que nous comptions utiliser pour mettre à jour les positions des joueurs, mais lorsque j'ai du implémenter l'affichage des joueurs, je me suis rendu compte que j'avais besoin d'obtenir les positions des joueurs dans l'actualisation du plateau, ce qui a mené à la modification de la fonction updateBoard pour y ajouter les positions des joueurs.
    Nous avions aussi une fonction updateCurrentPlayer qui nous permettait de mettre à jour le joueur actif, mais lorsque j'ai du implémenter l'affichage du joueur actif, je me suis rendu compte que j'avais besoin d'obtenir le nom du joueur actif dans l'actualisation du plateau, ce qui a mené à la modification de la fonction updateBoard pour y ajouter le nom du joueur actif.

Problèmes rencontrés : 
- Les déplacements des joueurs étaient décalés, cliquer sur une fleche faisait bouger le joueur dans une autre direction.
- Les directions de déplacement autorisées n'étaient pas les bonnes. Je me suis rendu compte que nous avions oublié une vérification dans la fonction changePossibleDirection.


