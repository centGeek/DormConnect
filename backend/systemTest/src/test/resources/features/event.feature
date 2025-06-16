Feature: Events

  Scenario:
    Given zalogowany użytkownik z mailem "user1@gmail.com" i haśle "haslo123"
    When dodaje event
    Then ma swój event

  Scenario: Approve an event
    Given zalogowany admin użytkownik z mailem "admin@edu.p.lodz.pl" i haśle "test"
    When zatwierdza event stworzony użytkownika user1@gmail.com
    Then event jest zatwierdzony

  Scenario: Get all waiting events
    Given zalogowany admin użytkownik z mailem "admin@edu.p.lodz.pl" i haśle "test"
    When pobiera oczekujące eventy
    Then otrzymuje listę oczekujących eventów

  Scenario: Join an event as participant
    Given zalogowany student użytkownik z mailem "student_debil@edu.p.lodz.pl" i haśle "test"
    When dołącza do eventu użytkownika user1@gmail.com
    Then dołączył do eventu

  Scenario: Leave an event as participant
    Given zalogowany student użytkownik z mailem "student_debil@edu.p.lodz.pl" i haśle "test"
    When opuszcza event użytkownika user1@gmail.com
    Then opuścił event