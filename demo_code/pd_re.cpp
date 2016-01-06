#include <cstdio>

using namespace std;

int s[10];

int main()
{
    int cases;
    scanf("%d", &cases);
    while( cases-- )
    {
        int a, b;
        scanf("%d%d", &a, &b);
        printf("%.10f\n", (double)a / b);
    }
    for( int i = 0; i < 10000000; i++ ) s[i] = 0;
    return 0;
}
