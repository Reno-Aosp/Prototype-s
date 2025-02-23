#include <stdio.h>
#include <stdlib.h>
#include <time.h>

// Global variables for pity counters and pull results
int pityCounter5Star = 0;  // Counter for 5-star pity (90 pulls)
int pityCounter4Star = 0;  // Counter for 4-star pity (10 pulls)
int totalPulls = 0;        // Total number of pulls made

// Function to simulate the RNG roll
const char* generateResult() {
    totalPulls++;  // Increment total pulls counter

    // Check if we need to guarantee a 5-star result after 89 pulls without a 5-star
    if (pityCounter5Star >= 89) {
        pityCounter5Star = 0;  // Reset pity counter after a guaranteed 5-star result
        pityCounter4Star = 0;  // Reset 4-star pity counter as well
        return "You got Guaranteed 5-Star item";  // Return a guaranteed 5-star
    }

    // Check if we need to guarantee a 4-star result after 9 pulls without a 4-star
    if (pityCounter4Star >= 9) {
        pityCounter4Star = 0;  // Reset pity counter after a guaranteed 4-star result
        pityCounter5Star++;    // Increase the 5-star pity counter for next possible 5-star
        return "You got Guaranteed 4-Star item";  // Return a guaranteed 4-star
    }

    // Soft pity logic for increasing chances of 5-star starting at pity 75
    double roll = (rand() % 100) / 100.0;  // Random number between 0 and 1

    // Soft pity logic for 5-star items (increases probability after pity 75)
    if (pityCounter5Star >= 75) {
        double softPityChance = 0.6 + (pityCounter5Star - 75) * 2;  // Incrementing chance for 5-star
        if (roll < softPityChance) {
            pityCounter5Star = 0;  // Reset pity counter on 5-star pull
            pityCounter4Star = 0;  // Reset 4-star pity counter as well
            return "You got 5-Star item (Soft Pity)";
        }
    }

    if (roll < 0.006) {  // 5-star rarity (0.6%)
        pityCounter5Star = 0;  // Reset pity counter on 5-star pull
        pityCounter4Star = 0;  // Reset 4-star pity counter as well
        return "You got 5-Star item";
    }

    // Soft pity logic for 4-star items (increases probability after pity 5)
    if (pityCounter4Star >= 5) {
        double softPity4StarChance = 0.054 + (pityCounter4Star - 6) * 100;  // Incrementing chance for 4-star
        if (roll < softPity4StarChance) {
            pityCounter4Star = 0;  // Reset 4-star pity counter
            pityCounter5Star++;    // Increase the 5-star pity counter
            return "You got 4-Star item (Soft Pity)";
        }
    }

    if (roll < 0.054) {  // 4-star rarity (5.4%)
        pityCounter4Star = 0;  // Reset 4-star pity counter on 4-star pull
        pityCounter5Star++;    // Increase 5-star pity counter
        return "You got 4-Star item";
    } else {  // 3-star rarity (94%)
        pityCounter4Star++;  // Increase 4-star pity counter
        pityCounter5Star++;  // Increase 5-star pity counter
        return "You got 3-Star item";
    }
}

// Function to perform a single pull
void pull() {
    printf("Pull Result: %s\n", generateResult());
    printf("5-Star Pity Counter: %d | 4-Star Pity Counter: %d\n", pityCounter5Star, pityCounter4Star);
}

// Function to perform 10 pulls at once
void pull10Times() {
    for (int i = 0; i < 10; i++) {
        printf("Pull %d: %s\n", i + 1, generateResult());
    }
    printf("5-Star Pity Counter: %d | 4-Star Pity Counter: %d\n", pityCounter5Star, pityCounter4Star);
}

int main() {
    srand(time(NULL));  // Initialize random seed based on current time

    int choice;

    while (1) {
        printf("\nRNG algorithm gacha with pity policy\n");
        printf("1. Pull\n");
        printf("2. Pull 10 Times\n");
        printf("3. Exit\n");
        printf("Enter your choice: ");
        scanf("%d", &choice);

        if (choice == 1) {
            pull();
        } else if (choice == 2) {
            pull10Times();
        } else if (choice == 3) {
            break;
        } else {
            printf("Invalid choice. Please try again.\n");
        }
    }

    return 0;
}
