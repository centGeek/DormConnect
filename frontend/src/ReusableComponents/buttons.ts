interface Button {
    text: string;
    link: string;
}

export const buttons: Button[] = [
    { text: 'Chat', link: '/chat' },
    { text: 'Wydarzenia', link: '/events' },
    { text: 'Pokoje wspólne', link: '/common-rooms' },
    { text: 'Pokój', link: '/rooms/myInfo' },
    { text: 'Zgłoś problem', link: '/problems' }
];