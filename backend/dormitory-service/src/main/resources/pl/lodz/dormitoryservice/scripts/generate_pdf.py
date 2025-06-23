import sys
import json
import base64
import asyncio
import requests
from datetime import datetime
from playwright.async_api import async_playwright
import io

def img_to_base64_from_file(path):
    with open(path, "rb") as f:
        return base64.b64encode(f.read()).decode()

def img_to_base64_from_url(url):
    r = requests.get(url)
    r.raise_for_status()
    return base64.b64encode(r.content).decode()

def get_static_map_url(lat, lon, zoom=15, width=400, height=300, marker=True):
    base = "https://staticmap.openstreetmap.de/staticmap.php"
    params = f"?center={lat},{lon}&zoom={zoom}&size={width}x{height}"
    if marker:
        params += f"&markers={lat},{lon},red-pushpin"
    return base + params

async def generate_pdf(html_content: str) -> bytes:
    async with async_playwright() as p:
        browser = await p.chromium.launch()
        page = await browser.new_page()
        await page.set_content(html_content, wait_until='networkidle')
        pdf_bytes = await page.pdf(format='A4', print_background=True, margin={"top":"2cm","bottom":"2cm","left":"2cm","right":"2cm"})
        await browser.close()
        return pdf_bytes

def create_html(data: dict, logo_base64: str, map_base64: str, lat, lon) -> str:
    now = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    map_img_html = ""
    if map_base64:
        map_img_html = f'''
        <div style="text-align:center; margin-top: 20px;">
            <img src="data:image/png;base64,{map_base64}" alt="Mapa lokalizacji" style="width:400px; height:300px; border:1px solid #666;" />
            <p style="font-size:12px; color:#666; margin-top: 5px;">Lokalizacja na mapie (lat: {lat}, lon: {lon})</p>
        </div>
        '''

    return f"""
    <!DOCTYPE html>
    <html lang="pl">
    <head>
        <meta charset="UTF-8" />
        <title>Potwierdzenie złożenia wniosku o akademik</title>
        <style>
            body {{
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 2cm;
                color: #444;
                background-color: #f8f8f8;
            }}
            h1 {{
                text-align: center;
                color: #222;
                margin-bottom: 1rem;
            }}
            table {{
                width: 100%;
                border-collapse: collapse;
                font-size: 14px;
                color: #333;
                background-color: #fff;
                box-shadow: 0 0 5px #ccc;
            }}
            th, td {{
                border: 1px solid #aaa;
                padding: 10px;
                text-align: center;
            }}
            th {{
                background-color: #666;
                color: #eee;
            }}
            tr:nth-child(even) {{
                background-color: #eee;
            }}
            p.footer {{
                margin-top: 3rem;
                font-style: italic;
                font-size: 12px;
                text-align: center;
                color: #666;
            }}
            .logo {{
                text-align: center;
                margin-top: 40px;
            }}
            .logo img {{
                width: 120px;
                opacity: 0.7;
            }}
        </style>
    </head>
    <body>
        <div class="logo">
            <img src="data:image/png;base64,{logo_base64}" alt="Logo Dorm Connect" />
        </div>

        <h1>Potwierdzenie złożenia wniosku o akademik</h1>
        <table>
            <tr><th>Pole</th><th>Wartość</th></tr>
            <tr><td>Data rozpoczęcia</td><td>{data.get('startDate', '-')}</td></tr>
            <tr><td>Data zakończenia</td><td>{data.get('endDate', '-')}</td></tr>
            <tr><td>Wynik priorytetu</td><td>{data.get('priorityScore', '-')}</td></tr>
            <tr><td>Dochód (PLN)</td><td>{data.get('income', '-')}</td></tr>
        </table>

        {map_img_html}

        <p class="footer">
            Dziękujemy za złożenie wniosku. Zostanie on rozpatrzony do 7 dni roboczych.<br/>
            Data wygenerowania dokumentu: {now}
        </p>
    </body>
    </html>
    """

def main():
    input_json = sys.stdin.read()
    try:
        data = json.loads(input_json)
    except Exception as e:
        print(f"Error: Niepoprawny JSON: {e}", file=sys.stderr)
        sys.exit(1)

    # Ładujemy logo z lokalnego pliku
    try:
        logo_base64 = img_to_base64_from_file("./dormitory-service/src/main/resources/pl/lodz/dormitoryservice/scripts/logo.png")
    except Exception as e:
        print(f"Warning: Nie udało się wczytać logo: {e}", file=sys.stderr)
        logo_base64 = ""

    lat = data.get('lat')
    lon = data.get('lon')
    map_base64 = ""

    if lat is not None and lon is not None:
        try:
            map_url = get_static_map_url(lat, lon)
            map_base64 = img_to_base64_from_url(map_url)
        except Exception as e:
            print(f"Warning: Nie udało się pobrać mapy: {e}", file=sys.stderr)

    html = create_html(data, logo_base64, map_base64, lat, lon)
    pdf_bytes = asyncio.run(generate_pdf(html))
    base64_pdf = base64.b64encode(pdf_bytes).decode('utf-8')
    print(base64_pdf)


if __name__ == "__main__":
    main()
