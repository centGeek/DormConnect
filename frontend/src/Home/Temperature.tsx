import { useEffect, useState } from 'react';
import './Temperature.css';

interface TemperatureProps {
  className?: string;
}

function Temperature({ className = '' }: TemperatureProps) {
  const [temperature, setTemperature] = useState<number | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchTemperature = async () => {
      try {
        const response = await fetch('/api/weather/temperature');
        
        if (!response.ok) {
          throw new Error('Failed to fetch temperature data');
        }
        
        const data = await response.json();
        setTemperature(data);
        setLoading(false);
      } catch (err) {
        console.error('Error fetching temperature:', err);
        setError('Unable to load temperature data');
        setLoading(false);
      }
    };

    fetchTemperature();
    
    // Odświeżaj temperaturę co 5 minut
    const intervalId = setInterval(fetchTemperature, 5 * 60 * 1000);
    
    return () => clearInterval(intervalId);
  }, []);

  if (loading) {
    return <span className={`temperature-display ${className}`}>Loading...</span>;
  }

  if (error) {
    return <span className={`temperature-display ${className}`}>--°C</span>;
  }

  return (
    <span className={`temperature-display ${className}`}>
      {temperature}°C
    </span>
  );
}

export default Temperature;