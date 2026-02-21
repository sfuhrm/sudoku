# Vorschlag: Wählbare Sudoku-Schwierigkeit + Schwierigkeitspunkte

## Zielbild

1. **Schwierigkeit bei Erstellung auswählen** (z. B. VERY_EASY bis VERY_HARD).
2. **Lösungswege bewerten** statt nur über Anzahl leerer Felder zu klassifizieren.
3. **Stabile Erzeugung**: Generator erzeugt ein Rätsel, Solver analysiert die notwendigen Techniken, danach wird in die Zielklasse einsortiert.

## Bereits umgesetzt (Basis)

- Neue API für die Erzeugung mit Schwierigkeit:
  - `Creator.createRiddle(fullMatrix, Difficulty)`
- Neue Enum `Difficulty` mit Schema-abhängigen Richtwerten für zu leerende Felder.

Damit kann bereits eine gewünschte Schwierigkeitsstufe beim Erzeugen angegeben werden.

## Vorschlag für Punkte-Modell

Die Schwierigkeit wird als Summe der im Solver benötigten Schritte berechnet.

### Einfache Techniken

- **Single in Zelle (Naked Single)**: 1 Punkt
- **Single in Einheit (Hidden Single in Zeile/Spalte/Block; „1 aus 9“) **: 2 Punkte

### Mittlere Techniken

- **Naked Pair/Triple**: 4 / 6 Punkte
- **Pointing Pair / Box-Line Reduction**: 5 Punkte

### Fortgeschritten

- **X-Wing**: 12 Punkte
- **Swordfish**: 18 Punkte
- **XY-Wing**: 15 Punkte

### Optional (Experte)

- **Färbung / Chains**: 20+ Punkte
- **Backtracking-Hinweis**: hoher Strafwert (z. B. +100), damit solche Rätsel in „Expert/Very Hard“ landen.

## Einstufungsvorschlag für 9x9

- **VERY_EASY**: 0–40 Punkte
- **EASY**: 41–90 Punkte
- **MEDIUM**: 91–160 Punkte
- **HARD**: 161–260 Punkte
- **VERY_HARD**: 261+

> Die Grenzwerte sollten mit einer Stichprobe (z. B. 1.000 Rätsel je Klasse) kalibriert werden.

## Technischer Ansatz zur Umsetzung

1. **Solver instrumentieren**
   - Jede angewendete Technik als `SolveStep` mit Typ, Position, Kandidaten und Punkten protokollieren.
2. **Score aggregieren**
   - `DifficultyScore = Summe(step.points)`
   - plus sekundäre Kennzahlen (Anzahl Schritte, maximale Technikstufe).
3. **Generator mit Feedback-Schleife**
   - Rätsel erzeugen → analysieren → bei Abweichung von Zielklasse weiter mutieren (mehr/weniger Felder leeren, gezielt Strukturen ändern).
4. **API-Erweiterung**
   - `CreationResult { Riddle riddle; DifficultyScore score; List<SolveStep> path; }`

## Nutzen

- Bessere Spielerwartung: „schwer“ bedeutet dann tatsächlich schwierigere Deduktion, nicht nur weniger Vorgaben.
- Erklärbarkeit: Für jedes Rätsel kann angezeigt werden, **welche** Techniken nötig waren.
- Gute Grundlage für „Hint“-Systeme, Lernmodus und Progression.
