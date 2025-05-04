import React from 'react';
import './Pagination.css';

interface PaginationProps {
    totalPages: number;
    currentPage: number;
    onPageChange: (page: number) => void;
}

const Pagination: React.FC<PaginationProps> = ({ totalPages, currentPage, onPageChange }) => {
    const pageNumbers = [];

    const maxVisiblePages = 3; // ile stron na raz pokazujemy (poza 1, ..., ostatnia)

    if (totalPages <= maxVisiblePages + 2) {
        // Jeśli mało stron — pokazujemy wszystkie
        for (let i = 0; i < totalPages; i++) {
            pageNumbers.push(i);
        }
    } else {
        // Początek
        pageNumbers.push(0);

        if (currentPage > 1) {
            pageNumbers.push(-1); // -1 = "..."
        }

        // Główne numery
        const startPage = Math.max(1, currentPage - 1);
        const endPage = Math.min(totalPages - 2, currentPage + 1);

        for (let i = startPage; i <= endPage; i++) {
            pageNumbers.push(i);
        }

        if (currentPage < totalPages - 2) {
            pageNumbers.push(-1); // -1 = "..."
        }

        // Koniec
        pageNumbers.push(totalPages - 1);
    }

    return (
        <div className="pagination">
            {pageNumbers.map((page, index) =>
                page === -1 ? (
                    <span key={index} className="dots">...</span>
                ) : (
                    <button
                        key={index}
                        className={`page-button ${currentPage === page ? 'active' : ''}`}
                        onClick={() => onPageChange(page)}
                    >
                        {page + 1}
                    </button>
                )
            )}
        </div>
    );
};

export default Pagination;
