Feature: Rejestracja i Logowanie

  Scenario: Rejestracja konta
    Given dane do rejestracji email i haśle "haslo123"
    When uzytkownik wykonuje rejestracje
    Then uzytkownik stworzyl konto studenta

  Scenario: Rejestracja konta jeśli już istnieje
    Given dane do rejestracji "user1@gmail.com" i haśle "haslo123"
    When uzytkownik próbuje się zarejestrować
    Then uzytkownik dostaje błąd

  Scenario: Logowanie poprawnymi danymi
    Given istnieje użytkownik "user1@gmail.com" i haśle "haslo123"
    When wykonuję logowanie do systemu
    Then uzytkownik ma dostęp do systemu

  Scenario: Wylogowanie użytkownika
    Given zalogowany użytkownik "user1@gmail.com" i haśle "haslo123" posiada token JWT
    When wylogowuje się z systemu
    Then jest wylogowany i nie posiada tokenu

