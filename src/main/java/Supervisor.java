//диспетчер процессов на основе SRT (shortest remaining time)

import java.util.Scanner;

public class Supervisor {
    public static void main(String[] args) {
        Process[] processes;  // массив процессов
        // переменная, отвечающая за ввод информации через консоль
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите кол-во процессов: ");
        int count = scanner.nextInt();
        while (count < 2 || count > 26) {
            System.out.print("Параметр задан неверно! Повторите ввод: ");
            count = scanner.nextInt();
        }
        System.out.println("Задать параметры случайным образом? 1 - Да 2 - Нет");
        int decision = scanner.nextInt();

        // заполнение массива процессов
        processes = (decision == 1) ? SetRandomProcesses(count) : SetManualProcesses(count);

        // создаём очередь
        Process[] queue = new Process[processes.length];
        int queueCounter = 0;
        for (int i = 0; i < processes.length; i++, queueCounter++) {
            // просматриваем весь массив и выбираем приоритетный процесс (SRT - самое короткое оставшееся время)
            // учитываются параметры иниц. и выполнения
            queue[i] = GetMinValueProc(processes);
        }

        //выполняем симуляцию
        System.out.println("Такт\tВыполняемый процесс\t\tОчередь");
        int tick = 0;
        for (int i = 0; i < queue.length; i++) {
            //инициализация процесса
            for (int j = 0; j < queue[i].initTime; j++, tick++) {
                System.out.println(tick + "\t\tinit " + queue[i].sym + "\t\t\t\t\t" + GetQueue(queue));
            }
            queue[i].initTime = -1; //убираем из очереди
            //выполнение процесса
            for (int j = 0; j < queue[i].procTime; j++, tick++) {
                System.out.println(tick + "\t\tprocessing " + queue[i].sym + "\t\t\t" + GetQueue(queue));
            }
            queue[i].procTime = -1; //убираем из очереди

        }
    }

    private static String GetQueue(Process[] queue) {
        StringBuilder res = new StringBuilder();
        for(int i=0;i<queue.length;i++){
            if(queue[i].initTime != -1){
                res.append(queue[i].sym);
            }
        }

        return res.toString();
    }

    //функция выдаёт минимальный по времени процесс
    private static Process GetMinValueProc(Process[] processes) {
        //ищем минимальный по суммарному времени элемент
        int minTime = 0;
        int k = 0;
        for (int i = 0; i < processes.length; i++)
            if (processes[i].procTime > 0 && processes[i].procTime > 0) {
                minTime = processes[i].initTime + processes[0].procTime;
                k = i; // запоминаем позицию минимального
                break;
            }

        for (int i = 0; i < processes.length; i++) {
            int thisTime = processes[i].initTime + processes[i].procTime;
            if (minTime > thisTime && thisTime > 0) {
                minTime = thisTime;
                k = i;
            }
        }
        //запоминаем
        Process minProc = new Process(processes[k]);
        //меняем знаки для пропуска на следующих итерациях
        processes[k].initTime *= -1;
        processes[k].procTime *= -1;

        return minProc;
    }

    // функция выполняет случайное заполенение массива процессов
    private static Process[] SetRandomProcesses(int countProc) {
        Process[] res = new Process[countProc];
        char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        for (int i = 0; i < countProc; i++) {
            int initTime = (int) (1 + Math.random() * 5);  // время инициализации
            int procTime = (int) (1 + Math.random() * 9); // время выполнения
            char sym = alphabet[i];
            System.out.println("Процесс " + alphabet[i]);
            System.out.println("\tвремя инициализации: " + initTime);
            System.out.println("\tвремя выполнения: " + procTime);

            res[i] = new Process(initTime, procTime, sym);
            System.out.print("\n");
        }

        return res;
    }

    // функция выполняет ручное заполенение массива процессов
    private static Process[] SetManualProcesses(int countProc) {
        Process[] res = new Process[countProc];
        char[] alphabet = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < countProc; i++) {
            System.out.println("Процесс " + alphabet[i]);
            System.out.print("\tвремя инициализации: ");
            int initTime = scanner.nextInt();  // время инициализации
            System.out.print("\tвремя выполнения: ");
            int procTime = scanner.nextInt();  // время выполнения
            char sym = alphabet[i];
            res[i] = new Process(initTime, procTime, sym);
            System.out.print("\n");
        }

        return res;
    }
}

class Process {
    public int initTime;
    public int procTime;
    public char sym;

    Process(int initTime, int procTime, char sym) {
        this.initTime = initTime;
        this.procTime = procTime;
        this.sym = sym;
    }

    Process(Process process){
        this.initTime = process.initTime;
        this.procTime = process.procTime;
        this.sym = process.sym;
    }

    public String toString(){
        return "Процесс '" + this.sym + "' " + this.initTime + " " + this.procTime;
    }
}