# Language‑Classifier – Jednowarstwowa sieć perceptronowa  

Prosty projekt w **Javie**, który uczy się rozpoznawać język (angielski, niemiecki, polski, hiszpański + dowolne kolejne) na podstawie dostarczonego tekstu.  
Model to *jednowarstwowa sieć perceptronowa*: każdy język ma własny perceptron, a klasyfikacja polega na wyborze tego z najwyższą aktywacją.

---

## Spis treści 
1. [Struktura projektu](#struktura-projektu)  
2. [Format danych](#format-danych)  
3. [Kompilacja i uruchomienie](#kompilacja-i-uruchomienie)  
4. [Dodawanie nowych języków](#dodawanie-nowych-j%C4%99zyk%C3%B3w)  
5. [Autor](#autor)

---

## Struktura projektu
```
MPP3_zad3_s25056/
├── resources/
│   ├── lang.test.csv
│   ├── lang.train.csv
│   ├── perceptron.data
│   └── perceptron.test.data
├── src/
│   ├── languageclassifier/
│   │   ├── LangMain.java          # Bazowy plik Main do uruchomienia sieci
│   │   ├── MultiLayerNetwork.java
│   │   └── MultiTeacher.java
│   └── perceptron/
│       ├── DiscretePerceptron.java
│       ├── Evaluator.java
│       ├── Main.java
│       ├── Perceptron.java
│       └── Teacher.java
└── MPP 03 - Ewaluacja.pdf

```

## Format danych
Każda linia pliku `.csv` ma **dwie** kolumny oddzielone przecinkiem:
```
<language>,<sentence>
```
Przykład:
```
english,"This is an example sentence"
german,"Das ist ein deutscher Satz"
polish,"To jest polskie zdanie"
spanish,"Esta es una frase española"
```
> Kolumna **1** – nazwa języka małą literą.  
> Kolumna **2** – dowolny tekst (w cudzysłowie, jeśli zawiera przecinki).

## Kompilacja i uruchomienie
### IntelliJ IDEA
1. **File → Open...** i wskazać katalog projektu.  
2. Ustaw *Working directory* na główny folder projektu (Edit Configurations → Working directory).  
3. Uruchom `LangMain`.


## Dodawanie nowych języków
1. **Dopisz wiersze** z nowym językiem w `lang.train.csv` **i** `lang.test.csv`.  
2. **Nie musisz zmieniać kodu**:        
   * `MultiTeacher` automatycznie doda nowy język do słownika `languageToIndex`.  
   * `MultiLayerNetwork` utworzy tyle perceptronów, ile języków znajdzie w train‑secie.

Po ponownym treningu model będzie potrafił rozpoznać wszystkie obecne w zbiorze treningowym języki.

## Autor
Projekt przygotowany na potrzeby zajęć **NAI** (PJATK) przez s25056.

