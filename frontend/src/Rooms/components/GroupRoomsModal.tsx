import React, { useState } from 'react';

interface GroupRoomsModalProps {
    onGroup: (groupName: string) => void;
    onClose: () => void;
}

const GroupRoomsModal: React.FC<GroupRoomsModalProps> = ({ onGroup, onClose }) => {
    const [groupName, setGroupName] = useState('');

    const handleSubmit = () => {
        if (groupName.trim() !== '') {
            onGroup(groupName);
        }
    };

    return (
        <div className="modal">
            <div className="modal-content">
                <h2>Podaj nazwę grupy</h2>
                <input
                    type="text"
                    value={groupName}
                    onChange={(e) => setGroupName(e.target.value)}
                    placeholder="Wpisz nazwę grupy"
                    style={{ marginBottom: '20px', width: '100%' }}
                />
                <div className="modal-buttons">
                    <button className="btn-primary" onClick={handleSubmit}>Grupuj</button>
                    <button className="btn-secondary" onClick={onClose}>Anuluj</button>
                </div>
            </div>
        </div>
    );
};

export default GroupRoomsModal;
