const CommonRoomTypes: Record<string, string> = {
    STUDY_ROOM: "Pokój nauki",
    GYM: "Siłownia",
    LAUNDRY: "Pralnia",
    BILLARD_ROOM: "Pokój bilardowy",
    TV_ROOM: "Pokój TV",
    FITNESS_ROOM: "Pokój fitness",
    TABLE_TENNIS_ROOM: "Tenis stołowy",
};

const getRoomStatusTranslation = (status: string): string => {
    return CommonRoomTypes[status] || status;
};

export default getRoomStatusTranslation;