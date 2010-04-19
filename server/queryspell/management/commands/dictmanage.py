
import sys
import codecs
from optparse import make_option

from django.core.management.base import BaseCommand
from django.core.exceptions import ObjectDoesNotExist

from queryspell.models import Dictionary, Words
from queryspell.trigram_tool import generate_trigram

class Command(BaseCommand):
    option_list = BaseCommand.option_list + (
        make_option('--list', action='store_true', dest='list', default=False,
            help='Show all dictionaries'),
        )
    args = "[action [dictname]]"
    
    def handle(self, action=None, dictname=None, filename=None, **options):
        if action == 'show':
            self.__show_dict(dictname)
        elif action == 'rm':
            if dictname:
                self.__rm_dict(dictname)
            else:
                print >> sys.stderr, "Error: you must provide a dictionary \
                    name or '*' for remove all dictionaries" % dictname
        elif action == 'rmall':
            self.__rm_dict()
        elif action == 'load':
            if not dictname:
                print 'You must name dictionary for loading data'
                print self.usage_str
                return

            if not filename:
                print 'Please provide a file with data for reading'
                print self.usage_str
                return
            self.__load_data(dictname, filename)
        elif action == 'index':
            self.__index()


    def __show_dict(self, dictname):
        def print_dict(dictionary):
            print ' Dictionary:   %s' % dictionary.name
            print ' Words number: %s' % \
                Words.objects.filter(dictionary=dictionary).count()
            print ' Indexed:      %s' % dictionary.get_indexed_status_display()
            print

        try:
            if dictname:
                dictionary = Dictionary.objects.get(name=dictname)
                print_dict(dictionary)
            else:
                dictionaries = Dictionary.objects.all()
                if not dictionaries:
                    print 'There are no dictionaries'
                for dictionary in dictionaries:
                    print_dict(dictionary)
        except ObjectDoesNotExist:
            print >> sys.stderr, "Error: dictionary `%s' not found" % dictname

    def __rm_dict(self, dictname=None):
        def rm_dict(dictionary):
            Words.objects.filter(dictionary=dictionary).delete()
            dictionary.delete()
            
        try:
            if not dictname:
                for dictionary in Dictionary.objects.all():
                    rm_dict(dictionary)
            else:
                dictionary = Dictionary.objects.get(name=dictname)
                rm_dict(dictionary)
        except ObjectDoesNotExist:
            print >> sys.stderr, "Error: dictionary `%s' not found" % dictname
            
        
    def __load_data(self, dictname, filename):
        dictionary, is_created = Dictionary.objects.get_or_create(name=dictname)
        if not is_created:
            print 'Removing old data from dictionary ...'
            Words.objects.filter(dictionary=dictionary).delete()
            dictionary.indexed_status = 'n'
            dictionary.save()

        line = ''
        added_word_count = 0
        try:
            in_file = codecs.open(filename, mode='r', encoding='utf-8')
            
            print 'Loading new data ...'

            for line_count, line in enumerate(in_file):
                if line:
                    word, freq = line.split()
                    if not word.isdigit():
                        added_word_count += 1
                        word = word.lower()
                        trigrams = u' '.join(generate_trigram(word))
                        Words.objects.create(word=word, trigrams=trigrams, \
                                             length=len(word), frequency=freq, \
                                             dictionary=dictionary)

            in_file.close()
        except ValueError, ex:
            print >> sys.stderr, "Error in %s line: bad file format (%s)\n`%s'" % \
                   (line_count, ex, line)
            return
        except IOError, ex:
            print >> sys.stderr, "Error: can't open file `%s'" % filename
            return

        print 'Complete: %s word(s) loaded into dictionary' % added_word_count


    def __generate_sphinx_conf(self):
        # TODO catch IOError
        SPHINX_CONF_FILE = getattr(settings, 'QUERYSPELL_SPHINX_CONF_FILE', \
                                   './queryspell_sphinx.conf')
        sphinx_conf_str = generate_sphinx_conf()
        out_file = codecs.open(SPHINX_CONF_FILE, mode='w', encoding='utf8')
        out_file.write(sphinx_conf_str)
        out_file.close()
            

    def __index(self):
        self.__generate_sphinx_conf()

        pass
