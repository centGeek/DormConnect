Feature: Rejestracja i Logowanie

  Scenario: Rejestracja konta
    Given dane do rejestracji "user1@gmail.com" i haśle "haslo123"
    When uzytkownik wykonuje rejestracje
    Then uzytkownik stworzyl konto studenta

  Scenario: Logowanie poprawnymi danymi
    Given istnieje użytkownik o username "user1@gmail.com" i haśle "haslo123"
    When wykonuję logowanie do systemu
    Then uzytkownik ma dostęp do systemu[

  Scenario: Wylogowanie użytkownika
    Given zalogowany użytkownik posiada token "valid_token"
    When wykonuję POST na "/api/auth/logout" z header Authorization "Bearer valid_token"
    Then odpowiedź ma status 200
    And odpowiedź zawiera "logged out"


  Scenario: Logowanie niepoprawanymi danymi
    Given nie istnieje uzytkowni o username "user2" i haśle "haslo123"


  Scenario:


  Scenario: