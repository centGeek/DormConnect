import { useTemperature } from '../Context/TemperatureContext.tsx';
import { Link } from 'react-router-dom';
import './HeaderButtonWithTemperature.css'; // optional styling

interface Props {
    text: string;
    link: string;
}

function HeaderButtonWithTemperature({ text, link }: Props) {
    const { temperature, loading, error } = useTemperature();

    const tempText = loading ? '...' : error ? '--°C' : `${temperature}°C`;
    const tooltip =
        !loading && !error && temperature !== null
            ? temperature > 10
                ? 'Good weather for Flanki!'
                : 'Bad weather for Flanki'
            : '';

    return (
        <div className="header-btn-with-temp">
            <Link to={link}>{text}</Link>
            <span className="header-temp" title={tooltip}>{tempText}</span>
        </div>
    );
}

export default HeaderButtonWithTemperature;
