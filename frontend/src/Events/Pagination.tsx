import React from 'react';

interface PaginationProps {
    totalPages: number;
    currentPage: number;
    onPageChange: (page: number) => void;
}

const Pagination: React.FC<PaginationProps> = ({ totalPages, currentPage, onPageChange }) => {
    const maxPagesToShow = 3;

    let startPage = Math.max(0, currentPage - Math.floor(maxPagesToShow / 2));
    let endPage = startPage + maxPagesToShow - 1;

    if (endPage >= totalPages) {
        endPage = totalPages - 1;
        startPage = Math.max(0, endPage - maxPagesToShow + 1);
    }

    const pageNumbers = [];
    for (let i = startPage; i <= endPage; i++) {
        pageNumbers.push(i);
    }

    return (
        <div className="flex justify-center items-center gap-2 bg-gray-100 p-3 mt-5">
            <button
                className="px-3 py-1 text-sm border border-gray-300 bg-white rounded hover:bg-gray-200 disabled:bg-gray-300 disabled:cursor-not-allowed"
                onClick={() => onPageChange(0)}
                disabled={currentPage === 0}
            >
                &lt;&lt;
            </button>
            <button
                className="px-3 py-1 text-sm border border-gray-300 bg-white rounded hover:bg-gray-200 disabled:bg-gray-300 disabled:cursor-not-allowed"
                onClick={() => onPageChange(currentPage - 1)}
                disabled={currentPage === 0}
            >
                &lt;
            </button>

            {pageNumbers.map(page => (
                <button
                    key={page}
                    className={`px-3 py-1 text-sm border border-gray-300 rounded hover:bg-gray-200 ${
                        currentPage === page ? 'bg-green-500 text-white' : 'bg-white'
                    }`}
                    onClick={() => onPageChange(page)}
                >
                    {page + 1}
                </button>
            ))}

            <button
                className="px-3 py-1 text-sm border border-gray-300 bg-white rounded hover:bg-gray-200 disabled:bg-gray-300 disabled:cursor-not-allowed"
                onClick={() => onPageChange(currentPage + 1)}
                disabled={currentPage === totalPages - 1}
            >
                &gt;
            </button>
            <button
                className="px-3 py-1 text-sm border border-gray-300 bg-white rounded hover:bg-gray-200 disabled:bg-gray-300 disabled:cursor-not-allowed"
                onClick={() => onPageChange(totalPages - 1)}
                disabled={currentPage === totalPages - 1}
            >
                &gt;&gt;
            </button>
        </div>
    );
};

export default Pagination;