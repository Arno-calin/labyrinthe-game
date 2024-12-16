all: build run

run: build
	- cd ./out && java App && cd ..
jar: build
	- cd ./out && jar cfe Labyrinthe.jar App ./* && cd ..

build:
	- rm -rf ./out
	- mkdir out
	- cd ./src && javac -d ../out/ ./*.java && cd ..
	- mkdir ./out/assets/
	- cp -r ./assets/images ./out/assets/images


clean:
	- rm -rf ./out
