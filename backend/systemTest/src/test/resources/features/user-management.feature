Feature: Zarządzanie użytkownikami

  Scenario: Aktualizacja nazwy użytkownika
    Given użytkownik istnieje z ID 1
    When wykonuję PUT na "/api/users/update/username" z body:
      """
      {
        "id": 1,
        "newUsername": "nowy_uzytkownik"
      }
      """
    Then odpowiedź ma status 200
    And odpowiedź zawiera "username updated"

  Scenario: Pobranie użytkownika po ID
    Given użytkownik istnieje z ID 1
    When wykonuję GET na "/api/users/get/1"
    Then odpowiedź ma status 200
    And odpowiedź zawiera "id": 1