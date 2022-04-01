# CPUTempTray

Shows CPU Temperature in Celsius

## Getting Started

This is a program that show a CPU temperature being as boll with number at tray.
Now it works only on MAC OS X.
Branch powermetrics contains changed code to expluatate mac os x integrated util powermetrics.
It works only with sudo credentials. 
To run this code you need to add environment variable SUDOPSWD=yoursudopassword in run configuration.
If all is good you will see the ball with numbers at tray.
To exit - just click right button of mouse on tray's icon.

Branch master contains code which gets a param from systemctl. In that branch you don't need sudo permission.
But temperature data is not so precise.

The minimum showing temperature is 1 grad Celsius and the max is 99.

### Prerequisites

You don't need to install any another tools. 
Use Intellij Idea or Eclipse or javac. 

## Running the tests

If you want you can write tests.

## Deployment

Just run

## Built With

* [BrainInHead](http://itisinheads/) :^)

## Authors

* **Sheenson Sergei** - *Initial work* - [SheensonSV](https://github.com/SheensonSV)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) void file for details

## Acknowledgments

JAVA