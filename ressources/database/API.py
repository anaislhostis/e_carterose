# -*- coding: utf-8 -*-

from flask import Flask, request, jsonify
import requests
import traceback
import pymysql
import json
import logging
import MySQLdb
import os
from datetime import datetime
from copy import deepcopy
import threading
from flask_sqlalchemy import SQLAlchemy


app = Flask(__name__)



distinct_elevage_numbers = [] # Liste pour stocker les numéros d'élevage distincts
elevages_local = [] # Liste pour stocker les elevages de la base locale
animaux_local = [] # Liste pour stocker les animaux de la base locale
soins_local = [] #Liste pour stocker les soins de la base locale
vaccins_local = [] #Liste pour stocker les vaccins de la base locale
elevages_remote_global = [] # Variable globale pour stocker les élevages distants
animaux_remote_global = [] # Variable globale pour stocker les animaux distants
soins_remote_global = [] # Variable globale pour stocker les soins distants
vaccins_remote_global = [] # Variable globale pour stocker les vaccins distants

# Définir l'URL de la base de données
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://username:password@host/database'

# Initialisation de l'extension SQLAlchemy
db = SQLAlchemy(app)

# Configurer les journaux
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

DATABASE_REMOTE = {
    'host': '10.10.64.12',
    'user': 'root',
    'password': 'L8hG1tR5',
    'database': 'bdd_ede'
}





#************************************************************************************************************************************************
#***************************************************************SERVER DATABASE******************************************************************
#************************************************************************************************************************************************
   
# Fonction pour se connecter à la base de données MySQL du server
def get_db_remote():
    db = getattr(threading.current_thread(), '_database_remote', None)
    if db is None:
        db = threading.current_thread()._database_remote = pymysql.connect(
            host=DATABASE_REMOTE['host'],
            user=DATABASE_REMOTE['user'],
            password=DATABASE_REMOTE['password'],
            database=DATABASE_REMOTE['database']
        )
    return db






#************************************************************************************************************************************************
#***************************************************************GET INFOS REMOTE*****************************************************************
#************************************************************************************************************************************************
    

#ANIMAUX
# API pour récupérer les animaux d'un élevage depuis la base de données MySQL du server 
@app.route("/animaux_remote/<num_elevage>", methods=['GET'])
def get_animaux_remote(num_elevage):
    try:
        conn = get_db_remote()
        if conn:
            cursor = conn.cursor()
            query = "SELECT * FROM animal WHERE num_elevage = %s"
            cursor.execute(query, (num_elevage,))
            remote_animals = cursor.fetchall()
            cursor.close()
            conn.close()
            
            # Parcourir les données renvoyées par la base de données distante
            synchronized_animals = []
            for animal_data in remote_animals:
                num_nat = animal_data[0]
                num_tra = animal_data[1]
                cod_pays = animal_data[2]
                nom = animal_data[3]
                sexe = animal_data[4]
                date_naiss = animal_data[5]
                cod_pays_naiss = animal_data[6]
                num_exp_naiss = animal_data[7]
                cod_pays_pere = animal_data[8]
                num_nat_pere = animal_data[9]
                cod_race_pere = animal_data[10]
                cod_pays_mere = animal_data[11]
                num_nat_mere = animal_data[12]
                cod_race_mere = animal_data[13]
                num_elevage = animal_data[14]
                cod_race = animal_data[15]
                date_modif = animal_data[17]
                actif = animal_data[16]
                
                # Ajouter les données de l'animal à la liste des animaux synchronisés
                synchronized_animals.append({
                    'num_nat': num_nat,
                    'num_tra': num_tra,
                    'cod_pays': cod_pays,
                    'nom': nom,
                    'sexe': sexe,
                    'date_naiss': str(date_naiss),  # Convertir la date en chaîne de caractères
                    'cod_pays_naiss': cod_pays_naiss,
                    'num_exp_naiss': num_exp_naiss,
                    'cod_pays_pere': cod_pays_pere,
                    'num_nat_pere': num_nat_pere,
                    'cod_race_pere': cod_race_pere,
                    'cod_pays_mere': cod_pays_mere,
                    'num_nat_mere': num_nat_mere,
                    'cod_race_mere': cod_race_mere,
                    'num_elevage': num_elevage,
                    'cod_race': cod_race,
                    'date_modif' : date_modif,
                    'actif' : actif
                })
            
            return jsonify(synchronized_animals)
        else:
            return jsonify({'error': 'Connexion à la base de données distante échouée'}), 500
    except Exception as e:
        logging.error('Erreur lors de la récupération des informations des animaux depuis la base de données distante : %s' % str(e))
        return jsonify({'error': str(e)}), 500



# API pour récupérer les animaux de tous les élevages depuis la base de données MySQL du server 
@app.route("/animaux_remote/", methods=['GET'])
def get_all_animaux_remote():
    try:
        conn = get_db_remote()
        if conn:
            cursor = conn.cursor()
            query = "SELECT * FROM animal"
            cursor.execute(query)
            remote_animals = cursor.fetchall()
            cursor.close()
            conn.close()

            to_update_data, to_add_data = compare_and_update_animaux()


            # Parcourir les données renvoyées par la base de données distante
            animals = []
            for animal_data in remote_animals: 
                # Ajouter les données de l'animal à la liste des animaux synchronisés
                #print("compare_and_update - animal_data : ")
                #print(animal_data)
        
                animal = {
                    'num_nat': animal_data[0],
                    'num_tra': animal_data[1],
                    'cod_pays': animal_data[2],
                    'nom': animal_data[3],
                    'sexe': animal_data[4],
                    'date_naiss': str(animal_data[5]),  
                    'cod_pays_naiss': animal_data[6],
                    'num_exp_naiss': animal_data[7],
                    'cod_pays_pere': animal_data[8],
                    'num_nat_pere': animal_data[9],
                    'cod_race_pere': animal_data[10],
                    'cod_pays_mere': animal_data[11],
                    'num_nat_mere': animal_data[12],
                    'cod_race_mere': animal_data[13],
                    'num_elevage': animal_data[14],
                    'cod_race': animal_data[15],
                    'actif' : animal_data[16],
                    'date_modif' : animal_data[17]
                }
                animals.append(animal)
            
            global animaux_remote_global
            animaux_remote_global = animals

            # Convertir les listes en JSON
            to_update_json = json.dumps(to_update_data, default=str)  # Utiliser default=str pour gérer les objets datetime
            to_add_json = json.dumps(to_add_data, default=str)  # Utiliser default=str pour gérer les objets datetime


            if to_update_json == "" and (to_add_json != ""):
                response_data = {
                    'to_update': [],
                    'to_add' : to_add_json,
                    'animaux_data': animals 
                }

            elif to_add_data == "" and (to_update_json != ""):
                response_data = {
                    'to_update': to_update_json,
                    'to_add' : [],
                    'animaux_data': animals 
                }
            
            elif to_add_json == "" and to_update_json == "" :
                response_data = {
                    'to_update': [],
                    'to_add' : [],
                    'animaux_data': animals 
                }

            else:
                # Inclure les données de compare_and_update_elevages dans la réponse JSON
                response_data = {
                    'to_update': to_update_json,
                    'to_add': to_add_json,
                    'animaux_data': animals 
                }

            return jsonify(response_data), 200

   
        else:
            return jsonify({'error': 'Connexion à la base de données distante échouée'}), 500
    except Exception as e:
        logging.error('Erreur lors de la récupération des informations des animaux depuis la base de données distante : %s' % str(e))
        return jsonify({'error': str(e)}), 500
    




# API pour récupérer les informations d'un animal par son numéro national depuis la base de données MySQL du server
@app.route("/animal_remote/<num_nat>", methods=['GET'])
def get_animal_remote(num_nat):
    try:
        conn = get_db_remote()
        cursor = conn.cursor()
        query = "SELECT * FROM animal WHERE num_nat = %s"
        cursor.execute(query, (num_nat,))
        animal = cursor.fetchone()
        if animal:
            return jsonify(animal)
        else:
            return jsonify({'message': 'Animal not found'}), 404
    except Exception as e:
        return jsonify({'error': str(e)}), 500




#ELEVAGES
# API pour récupérer les informations d'un élevage par son numéro depuis la base de données MySQL du server
@app.route("/elevage_remote/<num_elevage>", methods=['GET'])
def get_elevage_remote(num_elevage):
    try:
        conn = get_db_remote()
        if conn:
            cursor = conn.cursor()
            query = "SELECT * FROM elevage WHERE num_elevage = %s"
            cursor.execute(query, (num_elevage,))
            elevage_data = cursor.fetchone()
            cursor.close()
            conn.close()
            
            if elevage_data:
                num_elevage = elevage_data[0]
                nom = elevage_data[1]
                mdp = elevage_data[2]
                cod_asda = elevage_data[3]
                actif = elevage_data[4]
                date_modif = elevage_data[5]
                
                elevage = {
                    'num_elevage': num_elevage,
                    'nom': nom,
                    'mdp': mdp,
                    'cod_asda': cod_asda,
                    'actif' : actif,
                    'date_modif' : date_modif
                }
                return jsonify(elevage)
            
            else:
                return jsonify({'message': 'Élevage not found'}), 404
        else:
            return jsonify({'error': 'Connexion à la base de données échouée'}), 500
    except Exception as e:
        return jsonify({'error': str(e)}), 500
    

# API qui récupère tous les élevages de la base de données distante
@app.route("/elevages_remote/", methods=['GET'])
def get_all_elevages_remote():
    try:
        conn = get_db_remote()
        if conn:
            cursor = conn.cursor()
            query = "SELECT * FROM elevage"
            cursor.execute(query)
            elevages_data = cursor.fetchall()
            cursor.close()
            conn.close()

            to_update_data, to_add_data = compare_and_update_elevages()


            elevages_list = []
            for elevage in elevages_data:
                elevage_dict = {
                    'num_elevage': elevage[0],
                    'nom': elevage[1],
                    'mdp': elevage[2],
                    'cod_asda': elevage[3],
                    'actif': elevage[4],
                    'date_modif': elevage[5]
                }
                elevages_list.append(elevage_dict)
            

            global elevages_remote_global
            elevages_remote_global = elevages_list  # Initialisation de la variable avec les données distantes

            # Convertir les listes en JSON
            to_update_json = json.dumps(to_update_data, default=str)  # Utiliser default=str pour gérer les objets datetime
            to_add_json = json.dumps(to_add_data, default=str)  # Utiliser default=str pour gérer les objets datetime


            response_data = {
                'to_update': to_update_json,
                'to_add': to_add_json,
                'elevage_data': elevages_list
            }

        return jsonify(response_data), 200
    
    except Exception as e:
        return jsonify({'error': str(e)}), 500
    



@app.route("/soins_remote/", methods=['GET'])
def get_soins_remote():
    try:
        conn = get_db_remote()
        if conn:
            cursor = conn.cursor()
            query = "SELECT * FROM soins"
            cursor.execute(query)
            soins_data = cursor.fetchall()
            cursor.close()
            conn.close()


            # Comparer les soins distants avec ceux du téléphone et obtenir la liste des soins à ajouter
            to_add_data = compare_and_update_soins()

            soins_list = []
            for soin in soins_data:
                soin_dict = {
                    'num_nat': soin[0],
                    'nom_soin': soin[1],
                    'dose': soin[2],
                    'date_soin': soin[3]
                }
                soins_list.append(soin_dict)

            global soins_remote_global
            soins_remote_global = soins_list  # Initialisation de la variable avec les données distantes

            to_add_json = json.dumps(to_add_data, default=str)  # Utiliser default=str pour gérer les objets datetime
            
            response_data = {
                'to_add': to_add_json,
                'soins_data': soins_list
            }
            
            print("to_add dans get_soins_remote : ", response_data['to_add'])

            return jsonify(response_data), 200  # Retourner la réponse JSON contenant les données et les soins à ajouter

    except Exception as e:
        return jsonify({'error': str(e)}), 500



@app.route("/vaccins_remote/", methods=['GET'])
def get_vaccins_remote():
    try:
        conn = get_db_remote()
        if conn:
            cursor = conn.cursor()
            query = "SELECT * FROM vaccins"
            cursor.execute(query)
            vaccins_data = cursor.fetchall()
            cursor.close()
            conn.close()


            # Comparer les vaccins distants avec ceux du téléphone et obtenir la liste des vaccins à ajouter
            to_add_data = compare_and_update_vaccins()

            vaccins_list = []
            for vaccin in vaccins_data:
                vaccin_dict = {
                    'num_nat': vaccin[0],
                    'nom_vaccin': vaccin[1],
                    'dose': vaccin[2],
                    'date_vaccin': vaccin[3]
                }
                vaccins_list.append(vaccin_dict)

            global vaccins_remote_global
            vaccins_remote_global = vaccins_list  # Initialisation de la variable avec les données distantes

            to_add_json = json.dumps(to_add_data, default=str)  # Utiliser default=str pour gérer les objets datetime
            
            response_data = {
                'to_add': to_add_json,
                'vaccins_data': vaccins_list
            }
            
            print("to_add dans get_vaccins_remote : ", response_data['to_add'])

            return jsonify(response_data), 200  # Retourner la réponse JSON contenant les données et les vaccins à ajouter

    except Exception as e:
        return jsonify({'error': str(e)}), 500




#************************************************************************************************************************************************
#***************************************************************GET INFO LOCAL*******************************************************************
#************************************************************************************************************************************************
    


#ANIMAUX
#API qui récupère des données de la base locale sur les animaux d'un élevage en fonction du num_elevage 
@app.route('/animaux_local/<num_elevage>', methods=['GET'])
def get_animaux_data():
    try:
        # Effectuer une requête GET vers l'URL externe
        response = requests.get('http://10.10.64.12:5000/animaux_local/<num_elevage>')
        
        # Vérifier si la requête a réussi
        if response.status_code == 200:
            # Convertir les données JSON de la réponse en format Python
            all_animals = response.json()
            
            # Traiter les données comme souhaité
            animal_data = []
            for animal in all_animals:
                animal_dict = {
                    'num_nat': animal.get('num_nat'),
                    'num_tra': animal.get('num_tra'),
                    'cod_pays': animal.get('cod_pays'),
                    'nom': animal.get('nom'),
                    'sexe': animal.get('sexe'),
                    'date_naiss': animal.get('date_naiss'),
                    'cod_pays_naiss': animal.get('cod_pays_naiss'),
                    'num_exp_naiss': animal.get('num_exp_naiss'),
                    'cod_pays_pere': animal.get('cod_pays_pere'),
                    'num_nat_pere': animal.get('num_nat_pere'),
                    'cod_race_pere': animal.get('cod_race_pere'),
                    'cod_pays_mere': animal.get('cod_pays_mere'),
                    'num_nat_mere': animal.get('num_nat_mere'),
                    'cod_race_mere': animal.get('cod_race_mere'),
                    'num_elevage': animal.get('num_elevage'),
                    'cod_race': animal.get('cod_race'),
                    'date_modif': animal.get('date_modif'),
                    'actif': animal.get('actif')
                }
                animal_data.append(animal_dict)
            
            # Retourner les données converties en format JSON avec le code de statut 200 (OK)
            return jsonify(animal_data), 200
        else:
            # Retourner un message d'erreur avec le code de statut de la réponse
            return jsonify({'error': f"Échec de la récupération des données : {response.status_code}"}), response.status_code
    except Exception as e:
        # Retourner une erreur interne du serveur avec le message d'erreur
        return jsonify({'error': str(e)}), 500
    


#API qui envoie sur la route /animaux_local/ tous les animaux de la base locale
@app.route('/animaux_local/', methods=['POST'])
def post_animaux_data():
    if request.method == 'POST':
        try:

            global animaux_local
            # Récupérer les données JSON envoyées par l'application locale
            animaux_local = request.json
           
            return jsonify({'message': 'Données reçues avec succès'}), 200
        except Exception as e:
            return jsonify({'error': str(e)}), 500
    else:
        return "Méthode non autorisée", 405



#ELEVAGES
@app.route('/elevages_local/', methods=['POST'])
def receive_elevages_from_local():
    if request.method == 'POST':
        try:
            # Recevoir les données JSON envoyées par l'application locale
            global elevages_local
            elevages_local = request.json
            
            
            return jsonify(elevages_local), 200
        except Exception as e:
            return jsonify({'error': str(e)}), 500
    else:
        return "Méthode non autorisée", 405




#SOINS
@app.route('/soins_local/', methods=['POST'])
def receive_soins_from_local():
    if request.method == 'POST':
        try:
            # Recevoir les données JSON envoyées par l'application locale
            soins_local_data = request.json

            global soins_local
            soins_local = soins_local_data

            print("Soins local :", soins_local)
            
            return jsonify({'message': 'Données des soins mises à jour avec succès'}), 200
        except Exception as e:
            error_message = traceback.format_exc() if traceback.format_exc() else 'Une erreur inconnue s\'est produite.'
            print("Erreur lors de la réception des soins locaux :", error_message)
            return jsonify({'error': error_message}), 500
    else:
        return "Méthode non autorisée", 405



#VACCINS
@app.route('/vaccins_local/', methods=['POST'])
def receive_vaccins_from_local():
    if request.method == 'POST':
        try:
            # Recevoir les données JSON envoyées par l'application locale
            vaccins_local_data = request.json

            global vaccins_local
            vaccins_local = vaccins_local_data

            print("Soins local :", vaccins_local)
            
            return jsonify({'message': 'Données des vaccins mises à jour avec succès'}), 200
        except Exception as e:
            error_message = traceback.format_exc() if traceback.format_exc() else 'Une erreur inconnue s\'est produite.'
            print("Erreur lors de la réception des vaccins locaux :", error_message)
            return jsonify({'error': error_message}), 500
    else:
        return "Méthode non autorisée", 405









# API pour récupérer les numéros d'élevage distincts
@app.route("/distinct_local_elevage_numbers", methods=['GET'])
def get_distinct_local_elevage_numbers():
    return jsonify({'distinct_elevage_numbers': distinct_elevage_numbers}), 200



# API pour recevoir les numéros d'élevage distincts envoyés par l'application locale
@app.route("/distinct_local_elevage_numbers/", methods=['POST'])
def receive_distinct_local_elevage_numbers():
    try:
        # Récupérer les données JSON envoyées par l'application locale
        data = request.json

        # Extraire les numéros d'élevage distincts de la requête
        received_numbers = data.get('distinct_elevage_numbers')

        # Vérifier si des numéros ont été reçus
        if received_numbers:
            # Ajouter les numéros d'élevage distincts à la liste existante
            distinct_elevage_numbers.extend(received_numbers)
            return jsonify({'message': 'Numéros d\'élevage distincts reçus avec succès'}), 200
        else:
            return jsonify({'error': 'Aucun numéro d\'élevage distinct reçu'}), 400
    except Exception as e:
        return jsonify({'error': str(e)}), 500










#************************************************************************************************************************************************
#***************************************************************COMPARE_AND_UPDATE***************************************************************
#************************************************************************************************************************************************
    
def compare_and_update_elevages():
    try:
        # Récupérer les informations des élevages locaux envoyées par l'application
        local_elevage_info_list = elevages_local
        
        # Initialiser les listes de mises à jour et d'ajouts
        to_update = []
        to_add = []
        
        # Parcourir chaque élevage distant
        for remote_elevage in elevages_remote_global:
            num_elevage_remote = remote_elevage['num_elevage']
            date_modif_remote = remote_elevage['date_modif']

            # Rechercher l'élevage distant dans la liste des élevages locaux
            found = False
            for local_elevage in local_elevage_info_list:
                if local_elevage['num_elevage'] == num_elevage_remote:
                    found = True
                    break
            
            # Si l'élevage distant n'est pas trouvé localement, l'ajouter à la liste to_add
            if not found:
                elevage_data = {
                    'num_elevage': remote_elevage['num_elevage'],
                    'nom': remote_elevage['nom'],
                    'mdp': remote_elevage['mdp'],
                    'cod_asda': remote_elevage['cod_asda'],
                    'actif': remote_elevage['actif'],
                    'date_modif': date_modif_remote
                }
                to_add.append(elevage_data)

        # Parcourir chaque élevage local
        for local_elevage in local_elevage_info_list:
            num_elevage = local_elevage['num_elevage']
            date_modif_local_str = local_elevage['date_modif']  # Assurez-vous que la date est une chaîne au format 'YYYY-MM-DD HH:MM:SS'
            date_modif_local = datetime.strptime(date_modif_local_str, '%Y-%m-%d %H:%M:%S')

            # Rechercher l'élevage dans la liste des élevages du serveur (distant)
            for remote_elevage in elevages_remote_global:
                if remote_elevage['num_elevage'] == num_elevage:
                    date_modif_remote = remote_elevage['date_modif']
                    
                    # Comparer les dates de modification
                    if date_modif_remote < date_modif_local:
                        print("La date de modification est plus ancienne sur le server.")
                        # Mettre à jour l'élevage sur le serveur 
                        update_data = {
                            'num_elevage': local_elevage.get('num_elevage'),
                            'nom': local_elevage.get('nom'),
                            'mdp': local_elevage.get('mdp'),
                            'cod_asda': local_elevage.get('cod_asda'),
                            'date_modif': date_modif_local_str,
                            'actif': local_elevage.get('actif')
                        }
                        update_elevage_on_server(update_data)
                        break  # Sortir de la boucle une fois que l'élevage est mis à jour

                    elif date_modif_remote > date_modif_local:
                        print("La date de modification est plus ancienne dans la base du téléphone.")
                        # Mettre à jour l'élevage local avec les valeurs du serveur 
                        update_data = {
                            'num_elevage': remote_elevage['num_elevage'],
                            'nom': remote_elevage['nom'],
                            'mdp': remote_elevage['mdp'],
                            'cod_asda': remote_elevage['cod_asda'],
                            'actif': remote_elevage['actif'],
                            'date_modif': date_modif_remote
                        }
                        to_update.append(update_data)
                        break  # Sortir de la boucle une fois que l'élevage local est identifié

                    # L'élevage existe déjà sur le serveur distant, aucune action nécessaire
                    break
            else:
                # L'élevage n'existe pas dans la base du téléphone (locale), l'ajouter à la liste des élevages à ajouter
                elevage_data = {
                   'num_elevage': remote_elevage['num_elevage'],
                    'nom': remote_elevage['nom'],
                    'mdp': remote_elevage['mdp'],
                    'cod_asda': remote_elevage['cod_asda'],
                    'actif': remote_elevage['actif'],
                    'date_modif': date_modif_remote
                }
                to_add.append(elevage_data)

        print('compare_and_update_elevages :')
        print("to_add: ", to_add)
        print("to_update : ", to_update)

        # Convertir les listes en JSON
        to_update_data = deepcopy(to_update)
        to_add_data = deepcopy(to_add)

        # Vider les listes après utilisation
        to_update.clear()
        to_add.clear()
        distinct_elevage_numbers.clear()
        
        return to_update_data, to_add_data
    
    except Exception as e:
        return {'error': str(e)}, 500
    



def compare_and_update_animaux():
    try:
        local_animaux_info_list = animaux_local
        
        to_update = []
        to_add = []
        
        for local_animal in local_animaux_info_list:
            #print("compare_and_update_animal - local_info_list : ")
            #print(local_animal)

            num_nat = local_animal['num_nat']
            date_modif_local_str = local_animal['date_modif']  # Assurez-vous que la date est une chaîne au format 'YYYY-MM-DD HH:MM:SS'
            date_modif_local = datetime.strptime(date_modif_local_str, '%Y-%m-%d %H:%M:%S')

            found = False
            for remote_animal in animaux_remote_global:
                if remote_animal['num_nat'] == num_nat:
                    #print("compare_and_update_animaux : ")
                    #print("Données de l'animal distant : ")
                    #print(remote_animal)

                    found = True
                    
                    date_modif_remote = remote_animal['date_modif']
                    
                    if date_modif_remote < date_modif_local:
                        print("La date de modification est plus ancienne dans la base du server.")
                         # Mettre à jour l'animal sur le serveur 
                        update_data = {
                            'num_nat': num_nat,
                            'num_tra': local_animal.get('num_tra'),
                            'cod_pays': local_animal.get('cod_pays'),
                            'nom': local_animal.get('nom'),
                            'sexe': local_animal.get('sexe'),
                            'date_naiss': local_animal.get('date_naiss'),
                            'cod_pays_naiss': local_animal.get('cod_pays_naiss'),
                            'num_exp_naiss': local_animal.get('num_exp_naiss'),
                            'cod_pays_pere': local_animal.get('cod_pays_pere', None),
                            'num_nat_pere': local_animal.get('num_nat_pere', None),
                            'cod_race_pere': local_animal.get('cod_race_pere', None),
                            'cod_pays_mere': local_animal.get('cod_pays_mere'),
                            'num_nat_mere': local_animal.get('num_nat_mere'),
                            'cod_race_mere': local_animal.get('cod_race_mere'),
                            'num_elevage': local_animal.get('num_elevage'),
                            'cod_race': local_animal.get('cod_race'),
                            'date_modif': date_modif_local_str,
                            'actif': local_animal.get('actif')
                        }
                        print("compare_and_update_animaux - update_data", update_data)
                        update_animal_on_server(update_data)
                        break

                    if date_modif_remote > date_modif_local:
                        print("La date de modification est plus ancienne dans la base du téléphone.")
                        # Mettre à jour l'élevage local avec les valeurs du serveur 
                        update_data = {
                            'num_nat': num_nat,
                            'num_tra': remote_animal.get('num_tra'),
                            'cod_pays': remote_animal.get('cod_pays'),
                            'nom': remote_animal.get('nom'),
                            'sexe': remote_animal.get('sexe'),
                            'date_naiss': remote_animal.get('date_naiss'),
                            'cod_pays_naiss': remote_animal.get('cod_pays_naiss'),
                            'num_exp_naiss': remote_animal.get('num_exp_naiss'),
                            'cod_pays_pere': remote_animal.get('cod_pays_pere', None),
                            'num_nat_pere': remote_animal.get('num_nat_pere', None),
                            'cod_race_pere': remote_animal.get('cod_race_pere', None),
                            'cod_pays_mere': remote_animal.get('cod_pays_mere'),
                            'num_nat_mere': remote_animal.get('num_nat_mere'),
                            'cod_race_mere': remote_animal.get('cod_race_mere'),
                            'num_elevage': remote_animal.get('num_elevage'),
                            'cod_race': remote_animal.get('cod_race'),
                            'date_modif': date_modif_remote,
                            'actif': remote_animal.get('actif')
                        }
                        to_update.append(update_data)
                    break
            
            if not found:
                print("L'animal n'est pas présent dans la base du server")
                # Aucune correspondance trouvée avec l'animal distant, ajouter à to_add en utilisant les infos locales
                animal_data = {
                    'num_nat': num_nat,
                    'num_tra': local_animal.get('num_tra'),
                    'cod_pays': local_animal.get('cod_pays'),
                    'nom': local_animal.get('nom'),
                    'sexe': local_animal.get('sexe'),
                    'date_naiss': local_animal.get('date_naiss'),
                    'cod_pays_naiss': local_animal.get('cod_pays_naiss'),
                    'num_exp_naiss': local_animal.get('num_exp_naiss'),
                    'cod_pays_pere': local_animal.get('cod_pays_pere', None),
                    'num_nat_pere': local_animal.get('num_nat_pere', None),
                    'cod_race_pere': local_animal.get('cod_race_pere', None),
                    'cod_pays_mere': local_animal.get('cod_pays_mere'),
                    'num_nat_mere': local_animal.get('num_nat_mere'),
                    'cod_race_mere': local_animal.get('cod_race_mere'),
                    'num_elevage': local_animal.get('num_elevage'),
                    'cod_race': local_animal.get('cod_race'),
                    'date_modif': date_modif_local_str,
                    'actif': local_animal.get('actif')
                }
                add_animal_to_server(animal_data)

        #Parcourir chaque animal du serveur
        for remote_animal in animaux_remote_global:
            num_nat_remote = remote_animal['num_nat']
            date_modif_remote = remote_animal['date_modif']
            

            # Rechercher l'animal distant dans la liste des animaux locaux
            found = False
            for local_animal in local_animaux_info_list:
                if local_animal['num_nat'] == num_nat_remote:
                    found = True
                    break
            
            # Si l'animal distant n'est pas trouvé localement, l'ajouter à la liste to_add
            if not found:
                print("L'animal n'est pas présent dans la base du téléphone")
                animal_data = {
                    'num_nat': remote_animal.get('num_nat'),
                    'num_tra': remote_animal.get('num_tra'),
                    'cod_pays': remote_animal.get('cod_pays'),
                    'nom': remote_animal.get('nom'),
                    'sexe': remote_animal.get('sexe'),
                    'date_naiss': remote_animal.get('date_naiss'),
                    'cod_pays_naiss': remote_animal.get('cod_pays_naiss'),
                    'num_exp_naiss': remote_animal.get('num_exp_naiss'),
                    'cod_pays_pere': remote_animal.get('cod_pays_pere', None),
                    'num_nat_pere': remote_animal.get('num_nat_pere', None),
                    'cod_race_pere': remote_animal.get('cod_race_pere', None),
                    'cod_pays_mere': remote_animal.get('cod_pays_mere'),
                    'num_nat_mere': remote_animal.get('num_nat_mere'),
                    'cod_race_mere': remote_animal.get('cod_race_mere'),
                    'num_elevage': remote_animal.get('num_elevage'),
                    'cod_race': remote_animal.get('cod_race'),
                    'date_modif': remote_animal.get('date_modif'),
                    'actif': remote_animal.get('actif')
                }
                
                print(animal_data)

                to_add.append(animal_data)

        # Imprime les listes pour le débogage
        print("compare_and_update_animaux : ")
        print("to_add : ", to_add)
        print("to_update : ", to_update)

        # Copie les données dans de nouvelles variables pour les retourner
        to_update_data = deepcopy(to_update)
        to_add_data = deepcopy(to_add)

        # Efface les listes pour éviter les duplications
        to_update.clear()
        to_add.clear()
        distinct_elevage_numbers.clear()
            
        return to_update_data, to_add_data
    
    except Exception as e:
        return {'error': str(e)}, 500
    


def compare_and_update_vaccins():
    try:
        # Récupérer les données des vaccins locales et distantes
        vaccins_local_data = vaccins_local
        vaccins_remote_data = vaccins_remote_global

        print("vaccins remote", vaccins_remote_global)
        print("vaccins local", vaccins_local_data)

        # Liste des vaccins à ajouter à la base de données distante
        to_add = []
        to_add_data = []

        for vaccin_local in vaccins_local_data:
            # Accéder à la date de vaccin dans le dictionnaire vaccin_local
            date_vaccin_str = vaccin_local['date_vaccin']
            # Convertir la date en datetime.date
            vaccin_local_date = datetime.strptime(date_vaccin_str, '%Y-%m-%d')

            found = False
            for vaccin_remote in vaccins_remote_data:
                # Comparaison des vaccins par numéro national, nom de vaccin, dose et date de vaccin
                if (vaccin_local['num_nat'] == vaccin_remote['num_nat'] and
                    vaccin_local['nom_vaccin'] == vaccin_remote['nom_vaccin'] and
                    vaccin_local['dose'] == vaccin_remote['dose'] and
                    vaccin_local_date == vaccin_remote['date_vaccin']):
                    found = True
                    break
            # Si le vaccin local n'est pas trouvé dans les vaccins distants
            if not found:
                add_vaccins_to_remote(vaccin_local)

        # Ajouter les nouveaux vaccins à la base de données distante
        for vaccin_remote in vaccins_remote_data:
            found = False
            for vaccin_local in vaccins_local_data:
                # Utiliser la même variable date_vaccin_str pour stocker la date de chaque vaccin local
                date_vaccin_str = vaccin_local['date_vaccin']
                vaccin_local_date = datetime.strptime(date_vaccin_str, '%Y-%m-%d').date()
                if (vaccin_local['num_nat'] == vaccin_remote['num_nat'] and
                    vaccin_local['nom_vaccin'] == vaccin_remote['nom_vaccin'] and
                    vaccin_local['dose'] == vaccin_remote['dose'] and
                    vaccin_local_date == vaccin_remote['date_vaccin']):
                    found = True
                    break

            # Si le vaccin distant n'est pas trouvé dans les vaccins locaux et n'est pas déjà dans to_add, l'ajouter à to_add
            if (not found) and (vaccin_remote not in to_add):
                date_str = vaccin_remote['date_vaccin'].strftime('%Y-%m-%d')
                vaccin_remote['date_vaccin'] = datetime.strptime(date_str, '%Y-%m-%d').strftime('%Y-%m-%d')
                to_add.append(vaccin_remote)
       
        # Indiquer que les mises à jour des vaccins ont été effectuées avec succès
        print("Mises à jour des vaccins effectuées avec succès.")
        
        # Retourner la liste des nouveaux vaccins à ajouter
        to_add_data = deepcopy(to_add)
        to_add.clear()

        return to_add_data

    except Exception as e:
        # Gérer les exceptions et renvoyer un message d'erreur
        error_message = traceback.format_exc() if traceback.format_exc() else 'Une erreur inconnue s\'est produite.'
        print("Erreur lors de la comparaison et de la mise à jour des vaccins :", error_message)
        return {'error': error_message}, 500





def compare_and_update_soins():
    try:
        # Récupérer les données des soins locales et distantes
        soins_local_data = soins_local
        soins_remote_data = soins_remote_global

        print("Soins remote", soins_remote_global)
        print("Soins local", soins_local_data)

        # Liste des soins à ajouter à la base de données distante
        to_add = []
        to_add_data = []

        for soin_local in soins_local_data:
            # Accéder à la date de soin dans le dictionnaire soin_local
            date_soin_str = soin_local['date_soin']
            # Convertir la date en datetime.date
            soin_local_date = datetime.strptime(date_soin_str, '%Y-%m-%d')

            found = False
            for soin_remote in soins_remote_data:
                # Comparaison des soins par numéro national, nom de soin, dose et date de soin
                if (soin_local['num_nat'] == soin_remote['num_nat'] and
                    soin_local['nom_soin'] == soin_remote['nom_soin'] and
                    soin_local['dose'] == soin_remote['dose'] and
                    soin_local_date == soin_remote['date_soin']):
                    found = True
                    break
            # Si le soin local n'est pas trouvé dans les soins distants
            if not found:
                add_soins_to_remote(soin_local)

        # Ajouter les nouveaux soins à la base de données distante
        for soin_remote in soins_remote_data:
            found = False
            for soin_local in soins_local_data:
                # Utiliser la même variable date_soin_str pour stocker la date de chaque soin local
                date_soin_str = soin_local['date_soin']
                soin_local_date = datetime.strptime(date_soin_str, '%Y-%m-%d').date()
                if (soin_local['num_nat'] == soin_remote['num_nat'] and
                    soin_local['nom_soin'] == soin_remote['nom_soin'] and
                    soin_local['dose'] == soin_remote['dose'] and
                    soin_local_date == soin_remote['date_soin']):
                    found = True
                    break

            # Si le soin distant n'est pas trouvé dans les soins locaux et n'est pas déjà dans to_add, l'ajouter à to_add
            if (not found) and (soin_remote not in to_add):
                date_str = soin_remote['date_soin'].strftime('%Y-%m-%d')
                soin_remote['date_soin'] = datetime.strptime(date_str, '%Y-%m-%d').strftime('%Y-%m-%d')
                to_add.append(soin_remote)
       
        # Indiquer que les mises à jour des soins ont été effectuées avec succès
        print("Mises à jour des soins effectuées avec succès.")
        
        # Retourner la liste des nouveaux soins à ajouter
        to_add_data = deepcopy(to_add)
        to_add.clear()

        return to_add_data

    except Exception as e:
        # Gérer les exceptions et renvoyer un message d'erreur
        error_message = traceback.format_exc() if traceback.format_exc() else 'Une erreur inconnue s\'est produite.'
        print("Erreur lors de la comparaison et de la mise à jour des soins :", error_message)
        return {'error': error_message}, 500







#************************************************************************************************************************************************
#***************************************************************UPDATE_ON_SERVER*****************************************************************
#************************************************************************************************************************************************


#ELEVAGE
def update_elevage_on_server(elevage):
    updated_data = {}  # Variable pour stocker les données mises à jour

    try:
        # Mettre à jour les informations de l'élevage sur le serveur en utilisant une requête SQL UPDATE
        conn = get_db_remote()
        if conn:
            
            # Rétablir la connexion MySQL
            conn.ping(reconnect=True)

            cursor = conn.cursor()
            update_query = "UPDATE elevage SET nom = %s, mdp = %s, date_modif = %s, actif = %s WHERE num_elevage = %s"
            num_elevage = elevage.get('num_elevage')
            nom = elevage.get('nom')
            mdp = elevage.get('mdp')
            date_modif_str = datetime.now()
            date_modif = date_modif_str.strftime("%Y-%m-%d %H:%M:%S")
            actif = elevage.get('actif')
           
            print('update_elevage_on_server :')
            print('num_elevage de update_data : ', num_elevage)
            print('nom de update_data : ', nom)
            print('mdp de update_data : ', mdp)
            print('actif de update_data : ', actif)
            print('date_modif de update_data : ', date_modif)
            
            # Afficher la requête SQL avec les paramètres
            #print('Requête SQL :', update_query(nom, mdp, date_modif, actif, num_elevage))
            
            # Exécuter la requête SQL en utilisant les paramètres
            cursor.execute(update_query, (nom, mdp, date_modif, actif, num_elevage))
            conn.commit()
            cursor.close()
            conn.close()
            
            print('ajout terminé')
            
            # Stocker les informations mises à jour dans la variable
            updated_data = {
                'num_elevage': num_elevage,  # Inclure le numéro d'élevage
                'nom': nom,
                'mdp': mdp,
                'date_modif': date_modif,
                'actif' : actif
            }
            
            
            # Convertir le dictionnaire en JSON et le retourner
            return json.dumps(updated_data)

    except MySQLdb.Error as mysql_error:
        error_msg = '\nErreur MySQL lors de la mise à jour de l\'élevage sur le serveur. Détails :'
        error_msg += '\nCode d\'erreur : {}'.format(mysql_error.args[0])
        error_msg += '\nMessage d\'erreur : {}'.format(mysql_error.args[1])
        error_msg += '\nParamètres de la requête : {}'.format(elevage)
        error_msg += '\nRequête SQL : {}'.format(update_query)
        logging.error(error_msg)

    except Exception as e:
        logging.exception("Erreur lors de la mise à jour de l'élevage sur le serveur.")
        
    # En cas d'erreur ou de non-exécution de la mise à jour, retourner un dictionnaire vide
    return json.dumps(updated_data)



#ANIMAL
def update_animal_on_server(animal):
    updated_data = {}  # Variable pour stocker les données mises à jour

    try:
        # Mettre à jour les informations de l'animal sur le serveur en utilisant une requête SQL UPDATE
        conn = get_db_remote()
        if conn:
            # Rétablir la connexion MySQL
            conn.ping(reconnect=True)

            cursor = conn.cursor()
            update_query = "UPDATE animal SET num_tra = %s, cod_pays = %s, nom = %s, sexe = %s, date_naiss = %s, cod_pays_naiss = %s, num_exp_naiss = %s, cod_pays_pere = %s, num_nat_pere = %s, cod_race_pere = %s, cod_pays_mere = %s, num_nat_mere = %s, cod_race_mere = %s, cod_race = %s, num_elevage = %s, date_modif = %s, actif = %s WHERE num_nat = %s"
            

            num_nat = animal.get('num_nat')
            num_tra = animal.get('num_tra')
            cod_pays = animal.get('cod_pays')
            nom = animal.get('nom')
            sexe = animal.get('sexe')
            date_naiss_str = animal.get('date_naiss')
            date_naiss = datetime.strptime(date_naiss_str, "%Y-%m-%d")
            cod_pays_naiss = animal.get('cod_pays_naiss')
            num_exp_naiss = animal.get('num_exp_naiss')
            cod_pays_pere = animal.get('cod_pays_pere', None)
            num_nat_pere = animal.get('num_nat_pere', None)
            cod_race_pere = animal.get('cod_race_pere', None)
            cod_pays_mere = animal.get('cod_pays_mere')
            num_nat_mere = animal.get('num_nat_mere')
            cod_race_mere = animal.get('cod_race_mere')
            num_elevage = animal.get('num_elevage')
            cod_race = animal.get('cod_race')
          
            date_modif_str = datetime.now()
            date_modif = date_modif_str.strftime("%Y-%m-%d %H:%M:%S")
            actif = animal.get('actif')

            print("update_animal_on_server : ")
            print("num_nat:", num_nat)
            print("num_tra:", num_tra)
            print("cod_pays:", cod_pays)
            print("nom:", nom)
            print("sexe:", sexe)
            print("date_naiss:", date_naiss)
            print("cod_pays_naiss:", cod_pays_naiss)
            print("num_exp_naiss:", num_exp_naiss)
            print("cod_pays_pere:", cod_pays_pere)
            print("num_nat_pere:", num_nat_pere)
            print("cod_race_pere:", cod_race_pere)
            print("cod_pays_mere:", cod_pays_mere)
            print("num_nat_mere:", num_nat_mere)
            print("cod_race_mere:", cod_race_mere)
            print("cod_race :", cod_race)
            print('num_elevage:', num_elevage)
            print("date_modif:", date_modif)
            print("actif:", actif)


            # Exécuter la requête SQL en utilisant les paramètres
            cursor.execute(update_query, (num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race, num_elevage, date_modif, actif, num_nat))
            conn.commit()
            cursor.close()
            conn.close()


            
            # Stocker les informations mises à jour dans la variable
            updated_data = {
                'num_nat': num_nat,
                'num_tra': num_tra,
                'cod_pays': cod_pays,
                'nom': nom,
                'sexe': sexe,
                'date_naiss': date_naiss_str,
                'cod_pays_naiss': cod_pays_naiss,
                'num_exp_naiss': num_exp_naiss,
                'cod_pays_pere': cod_pays_pere,
                'num_nat_pere': num_nat_pere,
                'cod_race_pere': cod_race_pere,
                'cod_pays_mere': cod_pays_mere,
                'num_nat_mere': num_nat_mere,
                'cod_race_mere': cod_race_mere,
                'date_modif': date_modif,
                'actif': actif
            }
            
            # Convertir le dictionnaire en JSON et le retourner
            return json.dumps(updated_data)

    except MySQLdb.Error as mysql_error:
        error_msg = '\nErreur MySQL lors de la mise à jour de l\'élevage sur le serveur. Détails :'
        error_msg += '\nCode d\'erreur : {}'.format(mysql_error.args[0])
        error_msg += '\nMessage d\'erreur : {}'.format(mysql_error.args[1])
        error_msg += '\nParamètres de la requête : {}'.format(animal)
        error_msg += '\nRequête SQL : {}'.format(update_query)
        logging.error(error_msg)

    except Exception as e:
        logging.exception("Erreur lors de la mise à jour de l'animal sur le serveur.")
        
    # En cas d'erreur ou de non-exécution de la mise à jour, retourner un dictionnaire vide
    return json.dumps(updated_data)



def add_animal_to_server(animal):
    try:
        # Insérer les informations de l'animal sur le serveur en utilisant une requête SQL INSERT
        conn = get_db_remote()
        if conn:
            # Rétablir la connexion MySQL
            conn.ping(reconnect=True)

            cursor = conn.cursor()
            insert_query = "INSERT INTO animal (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race, num_elevage, date_modif, actif) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"

            # Extraire les données de l'animal
            num_nat = animal.get('num_nat')
            num_tra = animal.get('num_tra')
            cod_pays = animal.get('cod_pays')
            nom = animal.get('nom')
            sexe = animal.get('sexe')
            date_naiss_str = animal.get('date_naiss')
            date_naiss = datetime.strptime(date_naiss_str, "%Y-%m-%d")
            cod_pays_naiss = animal.get('cod_pays_naiss')
            num_exp_naiss = animal.get('num_exp_naiss')
            cod_pays_pere = animal.get('cod_pays_pere', None)
            num_nat_pere = animal.get('num_nat_pere', None)
            cod_race_pere = animal.get('cod_race_pere', None)
            cod_pays_mere = animal.get('cod_pays_mere')
            num_nat_mere = animal.get('num_nat_mere')
            cod_race_mere = animal.get('cod_race_mere')
            cod_race = animal.get('cod_race')
            num_elevage = animal.get('num_elevage')
            date_modif_str = animal.get('date_modif')
            date_modif = datetime.strptime(date_modif_str, "%Y-%m-%d %H:%M:%S")
            actif = animal.get('actif')

            # Exécuter la requête SQL en utilisant les paramètres
            cursor.execute(insert_query, (num_nat, num_tra, cod_pays, nom, sexe, date_naiss, cod_pays_naiss, num_exp_naiss, cod_pays_pere, num_nat_pere, cod_race_pere, cod_pays_mere, num_nat_mere, cod_race_mere, cod_race, num_elevage, date_modif, actif))
            conn.commit()
            cursor.close()
            conn.close()
            
            # Retourner les données de l'animal ajouté sous forme de JSON
            added_data = {
                'num_nat': num_nat,
                'num_tra': num_tra,
                'cod_pays': cod_pays,
                'nom': nom,
                'sexe': sexe,
                'date_naiss': date_naiss_str,
                'cod_pays_naiss': cod_pays_naiss,
                'num_exp_naiss': num_exp_naiss,
                'cod_pays_pere': cod_pays_pere,
                'num_nat_pere': num_nat_pere,
                'cod_race_pere': cod_race_pere,
                'cod_pays_mere': cod_pays_mere,
                'num_nat_mere': num_nat_mere,
                'cod_race_mere': cod_race_mere,
                'cod_race': cod_race,
                'date_modif': date_modif_str,
                'actif': actif
            }
            
            return json.dumps(added_data)

    except MySQLdb.Error as mysql_error:
        error_msg = '\nErreur MySQL lors de l\'ajout de l\'animal sur le serveur. Détails :'
        error_msg += '\nCode d\'erreur : {}'.format(mysql_error.args[0])
        error_msg += '\nMessage d\'erreur : {}'.format(mysql_error.args[1])
        error_msg += '\nParamètres de la requête : {}'.format(animal)
        error_msg += '\nRequête SQL : {}'.format(insert_query)
        logging.error(error_msg)

    except Exception as e:
        logging.exception("Erreur lors de l'ajout de l'animal sur le serveur.")

    # En cas d'erreur ou de non-exécution de l'ajout, retourner un dictionnaire vide
    return json.dumps({})




def add_elevage_to_server(elevage):
    try:
        # Insérer les informations de l'elevage sur le serveur en utilisant une requête SQL INSERT
        conn = get_db_remote()
        if conn:
            # Rétablir la connexion MySQL
            conn.ping(reconnect=True)

            cursor = conn.cursor()
            insert_query = "INSERT INTO elevage (nom, mdp, cod_asda, actif, date_modif) VALUES (%s, %s, %s, %s, %s)"

            # Extraire les données de l'elevage
            nom = elevage.get('nom')
            mdp = elevage.get('mdp')
            cod_asda = elevage.get('cod_asda')
            actif = elevage.get('actif')
            date_modif = elevage.get('date_modif')


            # Exécuter la requête SQL en utilisant les paramètres
            cursor.execute(insert_query, (nom,mdp,cod_asda,actif,date_modif))
            conn.commit()
            cursor.close()
            conn.close()
            
            # Retourner les données de l'elevage ajouté sous forme de JSON
            added_data = {
                'nom' : nom,
                'mdp' : mdp,
                'cod_asda' : cod_asda,
                'date_modif' : date_modif,
                'actif':actif
            }
            
            return json.dumps(added_data)



    except MySQLdb.Error as mysql_error:
        error_msg = '\nErreur MySQL lors de l\'ajout de l\'elevage sur le serveur. Détails :'
        error_msg += '\nCode d\'erreur : {}'.format(mysql_error.args[0])
        error_msg += '\nMessage d\'erreur : {}'.format(mysql_error.args[1])
        error_msg += '\nParamètres de la requête : {}'.format(elevage)
        error_msg += '\nRequête SQL : {}'.format(insert_query)
        logging.error(error_msg)

    except Exception as e:
        logging.exception("Erreur lors de l'ajout de l'elevage sur le serveur.")

    # En cas d'erreur ou de non-exécution de l'ajout, retourner un dictionnaire vide
    return json.dumps({})





#VACCINS
def add_vaccins_to_remote(vaccin_local):
    try:
        # Connexion à la base de données distante
        conn = get_db_remote()
        conn.ping(reconnect=True)

        cursor = conn.cursor()

        # Comparaison avec les données du serveur
       
        num_nat = vaccin_local.get('num_nat')
        nom_vaccin = vaccin_local.get('nom_vaccin')
        dose = vaccin_local.get('dose')
        date_vaccin_str = vaccin_local.get('date_vaccin')
        
        # Vérifier si toutes les valeurs nécessaires sont présentes
        if num_nat is not None and nom_vaccin is not None and dose is not None and date_vaccin_str is not None:
            # Convertir la date au format requis (%d-%m-%Y) avant de l'insérer dans la base de données
            date_vaccin = datetime.strptime(date_vaccin_str, '%Y-%m-%d').strftime('%Y-%m-%d')

            # Exécuter la requête SQL pour vérifier si le vaccin existe déjà dans la base de données
            sql_check_existence = "SELECT * FROM vaccins WHERE num_nat = %s AND nom_vaccin = %s AND dose = %s AND date_vaccin = %s"
            cursor.execute(sql_check_existence, (num_nat, nom_vaccin, dose, date_vaccin))
            existing_record = cursor.fetchone()

            if existing_record:
                print("Le vaccin existe déjà dans la base de données distante.")
            else:
                # Exécuter la requête SQL pour insérer le nouveau vaccin
                sql_insert = "INSERT INTO vaccins (num_nat, nom_vaccin, dose, date_vaccin) VALUES (%s, %s, %s, %s)"
                cursor.execute(sql_insert, (num_nat, nom_vaccin, dose, date_vaccin))
                print("Le nouveau vaccin a été ajouté à la base de données distante.")

        # Commit des changements dans la base de données distante
        conn.commit()
        print("add_vaccin_to_remote : Données des vaccins ajoutées avec succès à la base de données distante.")
    
    except Exception as e:
        error_message = traceback.format_exc() if traceback.format_exc() else 'Une erreur inconnue s\'est produite.'
        print("add_vaccin_to_remote : Erreur lors de l'ajout des vaccins à la base de données distante :", error_message)
        conn.rollback()
        return jsonify({'error': error_message}), 500
    finally:
        if conn:
            conn.close()





#SOINS
def add_soins_to_remote(soin_local):
    try:
        # Connexion à la base de données distante
        conn = get_db_remote()
        conn.ping(reconnect=True)

        cursor = conn.cursor()

        # Comparaison avec les données du serveur
       
        num_nat = soin_local.get('num_nat')
        nom_soin = soin_local.get('nom_soin')
        dose = soin_local.get('dose')
        date_soin_str = soin_local.get('date_soin')
        
        # Vérifier si toutes les valeurs nécessaires sont présentes
        if num_nat is not None and nom_soin is not None and dose is not None and date_soin_str is not None:
            # Convertir la date au format requis (%d-%m-%Y) avant de l'insérer dans la base de données
            date_soin = datetime.strptime(date_soin_str, '%Y-%m-%d').strftime('%Y-%m-%d')

            # Exécuter la requête SQL pour vérifier si le soin existe déjà dans la base de données
            sql_check_existence = "SELECT * FROM soins WHERE num_nat = %s AND nom_soin = %s AND dose = %s AND date_soin = %s"
            cursor.execute(sql_check_existence, (num_nat, nom_soin, dose, date_soin))
            existing_record = cursor.fetchone()

            if existing_record:
                print("Le soin existe déjà dans la base de données distante.")
            else:
                # Exécuter la requête SQL pour insérer le nouveau soin
                sql_insert = "INSERT INTO soins (num_nat, nom_soin, dose, date_soin) VALUES (%s, %s, %s, %s)"
                cursor.execute(sql_insert, (num_nat, nom_soin, dose, date_soin))
                print("Le nouveau soin a été ajouté à la base de données distante.")

        # Commit des changements dans la base de données distante
        conn.commit()
        print("add_soin_to_remote : Données des soins ajoutées avec succès à la base de données distante.")
    
    except Exception as e:
        error_message = traceback.format_exc() if traceback.format_exc() else 'Une erreur inconnue s\'est produite.'
        print("add_soin_to_remote : Erreur lors de l'ajout des soins à la base de données distante :", error_message)
        conn.rollback()
        return jsonify({'error': error_message}), 500
    finally:
        if conn:
            conn.close()




#************************************************************************************************************************************************
#********************************************************************MAIN************************************************************************
#************************************************************************************************************************************************


if __name__ == "__main__":
    # Démarrer le serveur Flask
    with app.app_context():
        # Déclencher la synchronisation périodique après le démarrage du serveur Flask
        app.run(host='0.0.0.0', port=5000) 

