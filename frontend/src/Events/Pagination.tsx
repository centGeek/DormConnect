import React from 'react';
import './Pagination.css';

interface PaginationProps {
    totalPages: number;
    currentPage: number;
    onPageChange: (page: number) => void;
}

const Pagination: React.FC<PaginationProps> = ({ totalPages, currentPage, onPageChange }) => {
    return (
        <div className="pagination">
            {Array.from({ length: totalPages }, (_, index) => (
                <button
                    key={index}
                    className={`page-button ${currentPage === index ? 'active' : ''}`}
                    onClick={() => onPageChange(index)}
                >
                    {index + 1}
                </button>
            ))}
        </div>
    );
};

export default Pagination;
