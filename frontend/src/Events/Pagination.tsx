import React from 'react';

interface PaginationProps {
    totalPages: number;
    currentPage: number;
    onPageChange: (page: number) => void;
}

const Pagination: React.FC<PaginationProps> = ({ totalPages, currentPage, onPageChange }) => {
    // Ograniczamy liczbę przycisków do trzech stron
    const maxPagesToShow = 3;

    // Obliczamy początkową stronę
    let startPage = Math.max(0, currentPage - Math.floor(maxPagesToShow / 2));
    let endPage = startPage + maxPagesToShow - 1;

    // Jeśli nie mamy wystarczająco stron z lewej strony, to przesuwamy startPage
    if (endPage >= totalPages) {
        endPage = totalPages - 1;
        startPage = Math.max(0, endPage - maxPagesToShow + 1);
    }

    const pageNumbers = [];
    for (let i = startPage; i <= endPage; i++) {
        pageNumbers.push(i);
    }

    return (
        <div className="pagination">
            <button
                className="page-button"
                onClick={() => onPageChange(0)} // Pierwsza strona
                disabled={currentPage === 0}
            >
                &lt;&lt;
            </button>
            <button
                className="page-button"
                onClick={() => onPageChange(currentPage - 1)} // Poprzednia strona
                disabled={currentPage === 0}
            >
                &lt;
            </button>

            {pageNumbers.map(page => (
                <button
                    key={page}
                    className={`page-button ${currentPage === page ? 'active' : ''}`}
                    onClick={() => onPageChange(page)}
                >
                    {page + 1}
                </button>
            ))}

            <button
                className="page-button"
                onClick={() => onPageChange(currentPage + 1)} // Następna strona
                disabled={currentPage === totalPages - 1}
            >
                &gt;
            </button>
            <button
                className="page-button"
                onClick={() => onPageChange(totalPages - 1)} // Ostatnia strona
                disabled={currentPage === totalPages - 1}
            >
                &gt;&gt;
            </button>
        </div>
    );
};

export default Pagination;
