#include <cstdio>
#include <cstdlib>
#include <cmath>

using namespace std;

const double eps = 1e-6;

int main(int argc, char *argv[])
{
    FILE *fp1 = fopen(argv[1], "r"), *fp2 = fopen(argv[2], "r");

    while(1)
    {
        double a, b;
        bool eof1 = (fscanf(fp1, "%lf", &a) == EOF), eof2 = (fscanf(fp2, "%lf", &b) == EOF);
        if( eof1 && eof2 ) break;
        else if( eof1 || eof2 ) exit(1);
        else
            if( fabs(a - b) < eps ) exit(1);
    }
    exit(0);
}
