import React from 'react';

type AddSlotProps = {
    onClick: () => void;
};

const AddSlot: React.FC<AddSlotProps> = ({ onClick }) => {
    return (
        <div className="add-slot" onClick={onClick}>
            +
        </div>
    );
};

export default AddSlot;
