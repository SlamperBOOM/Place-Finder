import placeFinderLib.*;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        PlaceFinder finder = new PlaceFinder();
        Scanner reader = new Scanner(System.in);
        String locale;
        do {
            System.out.print("Выберите язык поиска(ru/en): ");
            locale = reader.nextLine();
        } while (!locale.equals("en") && !locale.equals("ru"));

        int id;
        boolean askingPlace = true;
        List<String> places = null;
        while(true) {
            if(askingPlace) {
                System.out.print("Напишите название места: ");
                String placeName = reader.nextLine();
                places = finder.findPlace(placeName, locale);
                if(places == null){
                    System.out.println("Не найдено результатов");
                    continue;
                }
                for (int i = 0; i < places.size(); ++i) {
                    System.out.println((i + 1) + ") " + places.get(i));
                }
                System.out.println();
                askingPlace = false;
            }
            try {
                System.out.print("Выберите место, наиболее подходящее вашему запросу(Введите 0, если нет подходящих вариантов): ");
                String numberText = reader.nextLine();
                id = Integer.parseInt(numberText);
                if(id == 0){
                    askingPlace = true;
                    places = null;
                    continue;
                }
                id--;
            }catch (NumberFormatException e){
                continue;
            }
            if(id >= 0 && id < places.size()){
                break;
            }
        }
        int tryCount = 0;
        while(true){
            if(tryCount > 4){
                System.out.println("Не удалось найти информацию по выбранной точке");
                return;
            }
            List<String> info = finder.findInfoAboutPlace(id);
            if(info == null){
                System.out.println("Повторная попытка");
                tryCount++;
            }else{
                System.out.println("\nПогода в выбранном месте:\n" + info.get(0) + "\n" + info.get(1));
                for(int i=2; i< info.size(); ++i){
                    System.out.println((i-1) +") " + info.get(i));
                }
                break;
            }
        }

    }
}
