vuelo('Salta','Cordoba',80).
vuelo('Cordoba','Formosa',120).
vuelo('Cordoba','Rosario',55).
vuelo('Cordoba','Neuquen',100).
vuelo('Cordoba','Mendoza',65).
vuelo('Formosa','Corrientes',50).
vuelo('Corrientes','Posadas',70).
vuelo('Corrientes', 'Parana',57).
vuelo('Corrientes','Santa Fe',93).
vuelo('Posadas','Parana',85).
vuelo('Santa Fe','Rosario',30).
vuelo('Rosario','Parana',25).
vuelo('Rosario','Buenos Aires',55).
vuelo('Rosario','Mendoza',175).
vuelo('Mendoza', 'Buenos Aires',85).
vuelo('Mendoza','Neuquen',83).
vuelo('Neuquen', 'Buenos Aires',90).
vuelo('Buenos Aires','Calafate',115).
vuelo('Buenos Aires', 'Puerto Madryn',75).
vuelo('Neuquen','Calafate',65).


%BUSQUEDA PRIMERO EN PROFUNDIDAD:
calculaRutaPP(Inicio, Meta, Sol2, Total):- buscarRutaPP(Inicio,Meta,[Inicio],Sol),reverse(Sol,Sol2),distanciaRecorrida(Sol2,0,Total), write(Total),write(Sol2).
% si el estado actual es la meta, devolver la ruta recorrida como
% solución
buscarRutaPP(Estado, Meta, Ruta, Ruta):-(Estado = Meta).
% Generar un sucesor del estado actual. Si no fue visitado agregarlo a
% la ruta
buscarRutaPP(Estado, Meta, Ruta, Sol):-
	sucesor(Estado, NuevoEstado),
	not(pertenece(NuevoEstado, Ruta)),
	buscarRutaPP(NuevoEstado, Meta, [NuevoEstado|Ruta], Sol).

%BUSQUEDA PRIMERO EN ANCHURA
calculaRutaPA(Inicio, Meta, Sol2,Total):- buscarRutaPA([[Inicio]],Meta,Sol),reverse(Sol, Sol2),distanciaRecorrida(Sol2,0,Total), write(Total),write(Sol2).
% si el estado actual es la meta, devolver la ruta al mismo
% solución
buscarRutaPA([[Estado|Ruta]|_],Meta,[Estado|Ruta]):-(Estado=Meta).

% Generar las rutas sucesoras y las agrega a Lista-Nodos, descargando la
% primera
buscarRutaPA([[Estado|Ruta]|OtrasRutas], Meta, Sol):-
	(Estado\=Meta),
	expandir([Estado|Ruta],NuevasRutas),
	concatenar(OtrasRutas,NuevasRutas,NuevaListaNodos),
	buscarRutaPA(NuevaListaNodos,Meta,Sol).

%concatenar
concatenar([],[],[]).
concatenar([H1|T1],L2,[H1|T3]):-concatenar(T1,L2,T3).
concatenar([],[H2|T2],[H2|T3]):-concatenar([],T2,T3).

%generamos las rutas sucesoras

expandir([Estado|Ruta],NuevasRutas):-
	findall([NuevoEstado,Estado|Ruta],
		 (sucesor(Estado, NuevoEstado),
		  not(pertenece(NuevoEstado, [Estado|Ruta]))),
		  NuevasRutas).

%expandir si no hay rutas sucesoras
expandir(_,[]).


sucesor(Estado, NuevoEstado):-vuelo(Estado, NuevoEstado,_); vuelo(NuevoEstado, Estado,_).

pertenece(E,[E|_]).
pertenece(E,[_|T]):-pertenece(E,T).




distanciaRecorrida([],Total,Total).
distanciaRecorrida([H1,H2|T],Suma,Total):-(vuelo(H1,H2,Distancia);vuelo(H2,H1,Distancia)),
	                                  Suma2 is Suma +Distancia,distanciaRecorrida([H2|T],Suma2,Total).
distanciaRecorrida([_|[]],Total,Total).
