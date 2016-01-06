#include <cstdio>

using namespace std;

int main()
{
    int cases;
    scanf("%d", &cases);
    while( cases-- )
    {
        int a, b;
        scanf("%d%d", &a, &b);
        printf("%.10f\n", a / b);
    }
    return 0;
}
