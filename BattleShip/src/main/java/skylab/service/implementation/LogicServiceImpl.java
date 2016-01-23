package skylab.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import skylab.service.LogicService;

//прочитати два рази мінімум!
@Service
public class LogicServiceImpl implements LogicService {
	// Подумав і вирішив що працювати з двовимірним масивом простіше
	private int[][] battleGroung = new int[10][10];

	// конструктор в якому виводиться на екран автоматично згенероване поле для
	// гри
	// класно генерується по одній клітинці відступів навколо кораблів вроді так
	// правильно
	// подивитьсь на результат можна викликавши в мейні тільки конструктор new
	// Logic() і все
	// хотів був реалізувати гру в консолі але побачив що це затягує час і на
	// вебі буде по іншому працювати
	// тому вирішив не реалізовувати
	// краще подумаю як до БД це чудо зберегти
	// суть конструктора розуміти не потрібно в проекті я видалю його
	// метод який повертає рандомне число у відрізку мін макс
	// що б ти розумів Math.random() повертає дробове число в діапазоні 0-1
	// для того щоб отримати число у відрізку наприклад 5-10
	// потрібно домножити те що повернув рандом на різницю між 5 і 10 і додати
	// манімальне значення тобто 5
	// при мінімальному значенні 0*5+5=5 і при максимальному 1*5+5=10
	// це все ми заукруглюємо Math.round() і приводимо до інта
	public int random(int min, int max) {
		return (int) (Math.round((Math.random() * (max - min) + min)));
	}

	// продвинутий рандом повертає рандомне число в тому ж відрізку, але це
	// число не має бути в лісті currentNumbers
	public int advancedRandom(int min, int max, List<Integer> currentNumbers) {
		// цикл запускається доки не буде виконаний return
		while (true) {
			// отримуємо результат виконання методу вище
			int result = random(min, max);
			// перевіряєм чи бува не в колекції наш результат якщо ні повертаємо
			// його
			if (!currentNumbers.contains(result)) {
				return result;
			}
		}
	}

	// медод який створює колекцію інтів з масиву інтів з одним нюансом
	// записується в колекцію індекси заповнених комірок масиву
	public List<Integer> getAllFilledCell(int[] allX) {
		// ініціалізуємо ліст інтів
		List<Integer> currentNumbers = new ArrayList<Integer>();
		// біжимо по масиву
		for (int i = 0; i < allX.length; i++) {
			// перевіряємо чи комірка не заповнена
			if (allX[i] != 0) {
				// додаємо індекс в ліст
				currentNumbers.add(i);
			}
		}
		return currentNumbers;
	}

	// метод який перевіряє чи можна з рандомного місця в рандомному напрямку
	// встановити корабель
	// на countOfMast палуб
	public boolean canPlace(int countOfMast, int x, int y, int way) {
		// починаємо з свіча по напрямку
		// напрямків є 4 від 0 до 3 включно
		switch (way) {
		case 0: {
			// перевірка чи цей корабель взагалі влізе до краю поля
			if (10 - x <= countOfMast) {
				return false;
			} else {
				// якщо влазить то перевіряємо чи там де він має стояти немає
				// інших кораблів
				for (int i = x; i < x + countOfMast; i++) {
					if (battleGroung[i][y] != 0) {
						return false;
					}
				}
				// якщо немає то встановлюємо його тут
				// зверни увагу що break писати не потрібно все що після ретурну
				// не буде виконано
				for (int i = x; i < x + countOfMast; i++) {
					battleGroung[i][y] = 1;
				}
				// якщо встановили повертаємо true
				// і так в кожному кейсі просто вони в різні сторони направлені
				// в одних х і у зменшуються в інших збільшуються
				return true;
			}
		}
		case 1: {
			if (10 - y <= countOfMast) {
				return false;
			} else {
				for (int i = y; i < y + countOfMast; i++) {
					if (battleGroung[x][i] != 0) {
						return false;
					}
				}
				for (int i = y; i < y + countOfMast; i++) {
					battleGroung[x][i] = 1;
				}
				return true;
			}
		}
		case 2: {
			if (y <= countOfMast) {
				return false;
			} else {
				for (int i = y; i > y - countOfMast; i--) {
					if (battleGroung[x][i] != 0) {
						return false;
					}
				}
				for (int i = y; i > y - countOfMast; i--) {
					battleGroung[x][i] = 1;
				}
				return true;
			}
		}
		case 3: {
			if (x <= countOfMast) {
				return false;
			} else {
				for (int i = x; i > x - countOfMast; i--) {
					if (battleGroung[i][y] != 0) {
						return false;
					}
				}
				for (int i = x; i > x - countOfMast; i--) {
					battleGroung[i][y] = 1;
				}
				return true;
			}
		}
		}
		return false;
	}

	// метод який додає поле навколо корабля
	public void addBorder() {
		// запускаємо два цикли по осям координат
		for (int i = 0; i < battleGroung.length; i++) {
			for (int j = 0; j < battleGroung[i].length; j++) {
				// перевіряюмо чи бува в якійсь точці немає куска корабля
				if (battleGroung[i][j] == 1) {
					// якщо є то починаємо додавати рамку
					// суть заключається в тому що ми перевіряємо чи не вилетимо
					// за межі масиву
					// а потім перевіряємо чи в тій стороні немає продовження
					// корабля або вже встановленого поля іншого корабля
					if (i != 0 && battleGroung[i - 1][j] == 0) {
						battleGroung[i - 1][j] = 2;
					}
					if (j != 0 && battleGroung[i][j - 1] == 0) {
						battleGroung[i][j - 1] = 2;
					}
					if (i != 9 && battleGroung[i + 1][j] == 0) {
						battleGroung[i + 1][j] = 2;
					}
					if (j != 9 && battleGroung[i][j + 1] == 0) {
						battleGroung[i][j + 1] = 2;
					}
					// тут трошки склажніше: додаються поля ще і по діагоналі
					// всі поля по одній клітинці від корабля
					if (i != 0 && j != 0 && battleGroung[i - 1][j - 1] == 0) {
						battleGroung[i - 1][j - 1] = 2;
					}
					if (i != 9 && j != 0 && battleGroung[i + 1][j - 1] == 0) {
						battleGroung[i + 1][j - 1] = 2;
					}
					if (i != 9 && j != 9 && battleGroung[i + 1][j + 1] == 0) {
						battleGroung[i + 1][j + 1] = 2;
					}
					if (i != 0 && j != 9 && battleGroung[i - 1][j + 1] == 0) {
						battleGroung[i - 1][j + 1] = 2;
					}
				}
			}
		}
	}

	// метод який встановлює один корабель з заданою кількістю палуб
	public void placeOneShip(int countOfMast) {
		// вводимо змінну яка відповідає за те що ми встановили корабель
		boolean isPlased = false;
		// запускаємо цикл який буде працювати доки не буде встановлений
		// корабель
		while (!isPlased) {
			// генеруємо рандомне значення від 0 до 9 по осі х
			int x = random(0, 9);
			// генеруємо напрямок в яку сторону буде розміщатись корабель
			int way = random(0, 3);
			// витягуємо масив у-ків по тому х що згенерувався
			int[] allX = battleGroung[x];
			// перетворюємо виществорений масив на ліст індексів де вже щось є
			List<Integer> currentNumbers = getAllFilledCell(allX);
			// якщо вже все запонено розмір = 10
			// то метод піде на ще один виток циклу вже нічого робитись не буде
			if (currentNumbers.size() != 10) {
				// якщо ж ще не все заповнено використовуємо продвинутий рандом
				// що б отримати точку для початку корабля
				// точно не там де щось є
				int y = advancedRandom(0, 9, currentNumbers);
				// використовуємо метод чи можна встановити корабель
				// з рандомно згенерованими параметрами
				// якщо методу не вдасться встановити корабель
				// метод знову почне все з початку
				// згенерує всі рандомні величини і зно спробує встановити
				// корабель
				if (canPlace(countOfMast, x, y, way)) {
					// якщо все добре і корабель встановлений то викликається
					// метод який додасть поле навколо корабля
					addBorder();
					// і зманна яка дуже відповідальна дасть спокій методу і
					// циклу
					isPlased = true;
				}
			}
		}
	}

	public void deleteBorder() {
		for (int i = 0; i < battleGroung.length; i++) {
			for (int j = 0; j < battleGroung[i].length; j++) {
				if (battleGroung[i][j] == 2) {
					battleGroung[i][j] = 0;
				}
			}
		}
	}

	public int[][] placeAllShips() {
		// обнуляємо поле класу створивши новий масив
		battleGroung = new int[10][10];
		// викликаємо створення кораблів
		placeOneShip(4);
		placeOneShip(3);
		placeOneShip(3);
		placeOneShip(2);
		placeOneShip(2);
		placeOneShip(2);
		placeOneShip(1);
		placeOneShip(1);
		placeOneShip(1);
		placeOneShip(1);
		deleteBorder();
		// повертаємо заповнений масив кораблями
		return battleGroung;
	}
}
