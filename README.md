# Timetable generator for Karolina Square Dance Club

## Problem statement
In Karolina, we dance both square and round dance with people ranging from complete beginners to advanced dancers. Despite dancing in two halls in parallel and having 4-hour-long club evenings, we cannot cover the whole range of dance levels and have to compromise. Furthermore, timetable clashes occur where people would prefer to be in both halls at the same time.

## Solution
To optimise the dance timetable, a survey was conducted to ascertain people's preferences for different levels of square and round dance. This programme implements a genetic algorithm to generate timetables that take into account people's preferences.

Features include:
* Clustering levels people are interested in, so they don't have to sit around, waiting in-between their preferred levels
* A person's attendance (the likelihood they are present at the club evening) can be specified. This is used to penalise timetables where there would not be enough dancers for a square tip.
* A person's "importance" can be specified - for example, someone who only attends one specific level can be assigned a low importance as they will attend the club evening no matter the timetable

## Input
### Preferences file
A CSV file named `members.csv` where each row represents a single person. The first column is their name, the following columns contain their preferences for the different dance levels, in this order: round dance (RD) beginner class, easy RD, intermediate RD, advanced RD, square dance (SD) Mainstream class, SD Mainstream dancing, SD Plus class, SD Plus dancing, SD A1 class, SD A1 dancing, SD A2 class, SD A2 dancing.

The following values are allowed to be used as a preference: chcem priorita 1, chcem priorita 2, chcem priorita 3, chcem priorita 4, rád(a), môžem, nechcem, neviem.

The last two columns contain the importance and attendance, respectively. Both are decimal numbers between 0 and 1.

### Parameters file
A Java .properties file named `parameters.properties`. The following parameters are mandatory:

| Parameter | Type | Explanation |
| --------- | ---- | ----------- |
| calculateFitnessOnly | boolean | When true, fitness of a timetable specified in currentTimetable is calculated only, no optimisation is performed |
| iterations | integer | When the optimisation is performed, this is the number of iterations (generations) |
| hours | integer | The number of hours the timetable has (note that the programme assumes dancing in two halls, so the timetable consists of `2 * hours` classes) |
| maxPointMutations | integer | The maximum number of point mutations performed in one iteration. One point mutation changes one randomly selected level into another level (also randomly selected). |
| startFromCurrentTimetable | boolean | When true, fitness calculation or optimisation are performed on the timetable specified in currentTimetable, otherwise a random timetable is generated |
| currentTimetable | string | This consists of `2 * hours` strings separated by a comma followed by a space. The following strings are allowed: RD\_PRIPRAVKA, RD\_LAHKY, RD\_STREDNY, RD\_TAZKY, SD\_MS\_VYUKA, SD\_MS\_TANCOVANIE, SD\_PLUS\_VYUKA, SD\_PLUS\_TANCOVANIE, SD\_A1\_VYUKA, SD\_A1\_TANCOVANIE, SD\_A2\_VYUKA, SD\_A2\_TANCOVANIE. |
| forceLevels | boolean | If true, the fitness of a timetable not containing the levels specified in levelsToForce is set to 0 |
| levelsToForce | string | See forceLevels, and currentTimetable for the format of the string |
| lastYearShouldHelpThisYear | boolean | When true, the fitness of a timetable in which the last year's beginner's class' preferred levels clash with this year's beginner's class' levels is set to 0 |
| timetableHolePenaltyStrategy | integer | 1 corresponds to Assume4HoursStrategy, 2 to LogisticFunctionStrategy. These are strategies for calculating the penalties for timetable "gaps", that is gaps between people's preferred levels. Assume4HoursStrategy assumes the timetables has 4 hours, LogisticFunctionStrategy allows other timetable sizes (and gives approximately the same results for 4-hour-long timetables as the Assume4HoursStrategy). See the corresponding Java classes for more details on the strategies. |
| CHCEM1, CHCEM2, CHCEM3, CHCEM4, RAD, MOZEM, NECHCEM, NEVIEM | integers | This is a mapping between a preference as expressed by people in the survey (see section 'Preferences file') and its integer value; the higher the value, the higher the preference. |
| thresholdUnwantedPreference | integer | When identifying timetable gaps, this specifies the preference at/below which a level is deemed uninteresting for a person (see timetableHolePenaltyStrategy) |
| thresholdWantedPreference | integer | When identifying timetable gaps, this specifies the preference at/above which a level is deemed interesting for a person (see timetableHolePenaltyStrategy) |
| penalty1UnwantedBetweenWanted | decimal | Related to Assume4HoursStrategy (see timetableHolePenaltyStrategy). The penalty for a one-hour gap. Should be between 0 and 1. |
| penalty2UnwantedBetweenWanted | decimal | Related to Assume4HoursStrategy (see timetableHolePenaltyStrategy). The penalty for a two-hour gap. Should be between 0 and 1. |
| logisticFunctionK | decimal | Related to LogisticFunctionStrategy (see timetableHolePenaltyStrategy). Specifies the slope of the logistic curve - the steeper the slope, the more gaps are penalised. See https://en.wikipedia.org/wiki/Logistic_function |
| logisticFunctionXNaught | decimal | Related to LogisticFunctionStrategy (see timetableHolePenaltyStrategy). Specifies the x-value of the midpoint of the logistic curve - the lower the value, the more gaps are penalised. See https://en.wikipedia.org/wiki/Logistic_function |
| isPripravkaAtBeginning | boolean | If true, the fitness of a timetable in which the beginner class levels are not during the first 2 hours is set to 0 |
| notEnoughSparesForSquare | integer | When the number of people who are likely to be present at the club evening and want to dance a particular square dance level is at/below this number, we penalise the timetable (penaltyNotEnoughSpares) as there is a risk of an incomplete square which may have to be completed from people who would prefer not to dance that level |
| incompleteSquare | integer | When the number of people who are likely to be present at the club evening and want to dance a particular square dance level is at/below this number, we penalise the timetable (penaltyIncomplete) for an incomplete square which will have to be completed from people who would prefer not to dance that level |
| penaltyNotEnoughSpares | decimal | Between 0 and 1. See notEnoughSparesForSquare. |
| penaltyIncomplete | decimal | Between 0 and 1. See incompleteSquare. |