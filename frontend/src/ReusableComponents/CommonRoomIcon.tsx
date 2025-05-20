const CommonRoomTypes: Record<string, string> = {
    STUDY_ROOM: "graduation_hat",
    GYM: "weights",
    LAUNDRY: "washing",
    BILLARD_ROOM: "pool-ball",
    TV_ROOM: "tv",
    FITNESS_ROOM: "treadmill",
    TABLE_TENNIS_ROOM: "table-tennis",
};

const getRoomIcon = (status: string): string => {
    return CommonRoomTypes[status] || status;
};

export default getRoomIcon;