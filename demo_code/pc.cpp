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
        printf("%d\n", a * b);
    }
    return 0;
}
