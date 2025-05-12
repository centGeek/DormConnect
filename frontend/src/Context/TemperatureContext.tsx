import { createContext, useContext, useState, useEffect, ReactNode } from 'react';

interface TemperatureContextType {
    temperature: number | null;
    loading: boolean;
    error: string | null;
}

const TemperatureContext = createContext<TemperatureContextType | undefined>(undefined);

export const TemperatureProvider = ({ children }: { children: ReactNode }) => {
    const [temperature, setTemperature] = useState<number | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchTemperature = async () => {
            try {
                const res = await fetch('/api/weather/temperature');
                if (!res.ok) throw new Error();
                const data = await res.json();
                setTemperature(data);
            } catch {
                setError('Unable to load temperature data');
            } finally {
                setLoading(false);
            }
        };

        fetchTemperature();
    }, []);

    return (
        <TemperatureContext.Provider value={{ temperature, loading, error }}>
            {children}
        </TemperatureContext.Provider>
    );
};

export const useTemperature = (): TemperatureContextType => {
    const context = useContext(TemperatureContext);
    if (context === undefined) {
        throw new Error('useTemperature must be used within a TemperatureProvider');
    }
    return context;
};
