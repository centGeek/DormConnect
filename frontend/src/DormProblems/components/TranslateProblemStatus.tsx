export default function TranslateProblemStatus(status: string | undefined): string {
    if (!status) return 'Nieznany';
    switch (status.toUpperCase()) {
        case 'ALL':
            return 'Wszystkie';
        case 'IN_PROGRESS':
            return 'W trakcie';
        case 'SUBMITTED':
            return 'Przyjęte';
        case 'RESOLVED':
            return 'Rozwiązane';
        case 'REJECTED':
            return 'Odrzucone';
        default:
            return status;
    }
}